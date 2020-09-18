package com.ivo.mrp.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.direct.ary.MpsAry;
import com.ivo.mrp.entity.direct.cell.MpsCell;
import com.ivo.mrp.entity.direct.lcm.MpsLcm;
import com.ivo.mrp.entity.MpsVer;
import com.ivo.mrp.repository.MpsAryRepository;
import com.ivo.mrp.repository.MpsCellRepository;
import com.ivo.mrp.repository.MpsLcmRepository;
import com.ivo.mrp.repository.MpsVerRepository;
import com.ivo.mrp.service.MpsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MpsServiceImpl implements MpsService {

    private MpsVerRepository mpsVerRepository;

    private MpsLcmRepository mpsLcmRepository;

    private MpsAryRepository mpsAryRepository;

    private MpsCellRepository mpsCellRepository;

    @Autowired
    public MpsServiceImpl(MpsVerRepository mpsVerRepository, MpsLcmRepository mpsLcmRepository,
                          MpsAryRepository mpsAryRepository, MpsCellRepository mpsCellRepository) {
        this.mpsVerRepository = mpsVerRepository;
        this.mpsLcmRepository = mpsLcmRepository;
        this.mpsAryRepository = mpsAryRepository;
        this.mpsCellRepository = mpsCellRepository;
    }

    @Override
    public MpsVer getMpsVer(String ver) {
        return mpsVerRepository.findById(ver).orElse(null);
    }

    @Override
    public void syncMpsLcm() {
        //TODO...
    }

    @Override
    public void syncMpsCell() {
        //TODO...
    }

    @Override
    public void syncMpsAry() {
        //TODO...
    }

    @Override
    public String generateMpsVer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = String.format("%03d", (mpsVerRepository.findAll().size()+1)%1000);
        return  sdf.format(new java.util.Date())+str;
    }

    @Override
    public void importMpsLcm(InputStream inputStream, String fileName, String user) {
        log.info("LCM MPS数据导入 >> START");
        List<java.sql.Date> titleName1 = new ArrayList<>();
        List<String> titleName2 = new ArrayList<>();
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        if(list == null || list.size() < 2) return;
        int rowInt = 0;
        int cellInt = 0;

        String mpsVerLcm1 = generateMpsVer();
        String mpsVerLcm2 = Long.toString(Long.parseLong(mpsVerLcm1) + 1);
        java.sql.Date startDate;
        java.sql.Date endDate;
        List<MpsLcm> mpsLcmList1 = new ArrayList<>();
        List<MpsLcm> mpsLcmList2 = new ArrayList<>();
        try {
            //前两行标题
            List<Object> row1 = list.get(rowInt);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
            for (; cellInt < row1.size(); cellInt++) {
                if (cellInt == 0) {
                    titleName1.add(null);
                    continue;
                }
                Object object = row1.get(cellInt);
                if (object == null) {
                    titleName1.add(titleName1.get(cellInt - 1));
                    continue;
                }
                java.sql.Date fabDate;
                if (object instanceof Date) {
                    fabDate = new java.sql.Date(sdf.parse(sdf.format(object)).getTime());
                } else if (object instanceof BigDecimal) {
                    Calendar calendar = new GregorianCalendar(1900, Calendar.JANUARY, -1);
                    Date d = calendar.getTime();
                    Date dd = DateUtils.addDays(d, Integer.valueOf(object.toString()));
                    fabDate = new java.sql.Date(sdf.parse(sdf.format(dd)).getTime());
                } else {
                    if(StringUtils.isEmpty((String) object)) {
                        titleName1.add(titleName1.get(cellInt - 1));
                        continue;
                    }
                    fabDate = new java.sql.Date(sdf.parse((String) object).getTime());
                }
                titleName1.add(fabDate);
            }
            startDate = titleName1.get(1);
            endDate = titleName1.get(titleName1.size()-1);

            rowInt++;
            cellInt = 0;
            List<Object> row2 = list.get(rowInt);
            for (; cellInt < row2.size(); cellInt++) {
                Object object = row2.get(cellInt);
                String str = object == null ? null : ((String) object).toUpperCase();
                titleName2.add(str);
            }
            //后面数据
            rowInt++;
            cellInt = 0;
            for (; rowInt < list.size(); rowInt++) {
                List<Object> row = list.get(rowInt);
                String fullName = (String) row.get(cellInt);
                //机种截取前面的版本
                String product;
                if(StringUtils.contains(fileName, "/")) {
                    product = fullName.substring(0, fullName.indexOf("/"));
                } else {
                    product = fullName;
                }
                cellInt++;
                for (; cellInt < row.size(); cellInt++) {
                    double demandQty=0;
                    Object object = row.get(cellInt);
                    if (object != null) {
                        if (object instanceof String) {
                            if (StringUtils.isNotEmpty((String) object)) {
                                demandQty = Double.valueOf((String) object);
                            }
                        } else if (object instanceof BigDecimal) {
                            demandQty = ((BigDecimal) object).doubleValue();
                        } else {
                            demandQty = (double) object;
                        }
                    }

                    java.sql.Date fabDate = titleName1.get(cellInt);
                    String fab = titleName2.get(cellInt);

                    MpsLcm mpsLcm = new MpsLcm();
                    mpsLcm.setProduct(product);
                    mpsLcm.setFullName(fullName);
                    mpsLcm.setFabDate(fabDate);
                    mpsLcm.setDemandQty(demandQty);
                    mpsLcm.setCreator(user);
                    if (StringUtils.equalsIgnoreCase(fab, "LCM1")) {
                        mpsLcm.setVer(mpsVerLcm1);
                        mpsLcmList1.add(mpsLcm);
                    } else {
                        mpsLcm.setVer(mpsVerLcm2);
                        mpsLcmList2.add(mpsLcm);
                    }
                }
                cellInt = 0;
            }
        }catch (Exception e) {
            throw new RuntimeException("EXCEL导入错误，第"+rowInt+"行，第"+cellInt+"列数据不准确."+e.getMessage());
        }

        MpsVer mpsVer1 = new MpsVer();
        mpsVer1.setVer(mpsVerLcm1);
        mpsVer1.setFab("LCM1");
        mpsVer1.setType(MpsVer.Type_Lcm);
        mpsVer1.setSource(MpsVer.Source_MC);
        mpsVer1.setStartDate(startDate);
        mpsVer1.setEndDate(endDate);
        mpsVer1.setCreator(user);

        MpsVer mpsVer2 = new MpsVer();
        mpsVer2.setVer(mpsVerLcm2);
        mpsVer2.setFab("LCM2");
        mpsVer2.setType(MpsVer.Type_Lcm);
        mpsVer2.setSource(MpsVer.Source_MC);
        mpsVer2.setStartDate(startDate);
        mpsVer2.setEndDate(endDate);
        mpsVer2.setCreator(user);

        mpsVerRepository.save(mpsVer1);
        mpsLcmRepository.saveAll(mpsLcmList1);
        mpsVerRepository.save(mpsVer2);
        mpsLcmRepository.saveAll(mpsLcmList2);
        log.info("LCM MPS数据导入 >> END");
    }

    @Override
    public void importMpsAry(InputStream inputStream, String fileName, String user) {
        //TODO...
    }

    @Override
    public void importMpsCell(InputStream inputStream, String fileName, String user) {
        //TODO...
    }

    @Override
    public List<String> getMpsLcmProduct(String mpdVer) {
        return mpsLcmRepository.getProduct(mpdVer);
    }

    @Override
    public List<String> getMpsAryProduct(String mpdVer) {
        return mpsAryRepository.getProduct(mpdVer);
    }

    @Override
    public List<String> getMpsCellProduct(String mpdVer) {
        return mpsCellRepository.getProduct(mpdVer);
    }

    @Override
    public List<MpsLcm> getMpsLcm(String mpsVer, String product) {
        return mpsLcmRepository.findByVerAndProduct(mpsVer, product);
    }

    @Override
    public List<MpsAry> getMpsAry(String mpsVer, String product) {
        return mpsAryRepository.findByVerAndProduct(mpsVer, product);
    }

    @Override
    public List<MpsCell> getMpsCell(String mpsVer, String product) {
        return mpsCellRepository.findByVerAndProduct(mpsVer, product);
    }

    @Override
    public List<MpsLcm> getMpsLcm(String mpsVer) {
        return mpsLcmRepository.findByVer(mpsVer);
    }

    @Override
    public List<MpsAry> getMpsAry(String mpsVer) {
        return mpsAryRepository.findByVer(mpsVer);
    }

    @Override
    public List<MpsCell> getMpsCell(String mpsVer) {
        return mpsCellRepository.findByVer(mpsVer);
    }

    @Override
    public Page<MpsVer> queryMpsVer(int page, int limit, String searchFab, String searchVer) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "ver");
        return mpsVerRepository.findByFabLikeAndVerLikeAndValidFlagIsTrue(searchFab+"%", searchVer+"%", pageable);
    }

    @Override
    public List getDpsDate(String ver) {
        MpsVer mpsVer = getMpsVer(ver);
        if(mpsVer == null) return new ArrayList();
        String type = mpsVer.getType();
        List list;
        switch (type) {
            case MpsVer.Type_Ary:
                list = getMpsAry(ver);
                break;
            case MpsVer.Type_Cell:
                list = getMpsCell(ver);
                break;
            case MpsVer.Type_Lcm:
                list = getMpsLcm(ver);
                break;
            default:
                list = new ArrayList();
        }
        return list;
    }
}
