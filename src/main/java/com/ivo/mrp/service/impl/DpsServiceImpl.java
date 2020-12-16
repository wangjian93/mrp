package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp.entity.*;
import com.ivo.mrp.entity.direct.ary.DpsAry;
import com.ivo.mrp.entity.direct.ary.DpsAryOc;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.cell.DpsCellOutputName;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.repository.*;
import com.ivo.mrp.service.packageing.BomPackageService;
import com.ivo.mrp.service.DpsOutputNameService;
import com.ivo.mrp.service.DpsService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class DpsServiceImpl implements DpsService {

    private DpsVerRepository dpsVerRepository;

    private DpsLcmRepository dpsLcmRepository;

    private DpsAryRepository dpsAryRepository;

    private DpsCellRepository dpsCellRepository;


    private DpsAryOcRepository dpsAryOcRepository;

    private RestService restService;


    private DpsOutputNameService dpsOutputNameService;

    @Autowired
    public DpsServiceImpl(DpsVerRepository dpsVerRepository, DpsLcmRepository dpsLcmRepository, DpsAryRepository dpsAryRepository,
                          DpsCellRepository dpsCellRepository,
                          DpsAryOcRepository dpsAryOcRepository,
                          RestService restService, BomPackageService bomPackageService, DpsOutputNameService dpsOutputNameService) {
        this.dpsVerRepository = dpsVerRepository;
        this.dpsLcmRepository = dpsLcmRepository;
        this.dpsAryRepository = dpsAryRepository;
        this.dpsCellRepository = dpsCellRepository;
        this.dpsAryOcRepository = dpsAryOcRepository;
        this.restService = restService;
        this.dpsOutputNameService = dpsOutputNameService;
    }

    @Override
    public DpsVer getDpsVer(String ver) {
        return dpsVerRepository.findById(ver).orElse(null);
    }

    @Override
    public List<DpsAry> getDpsAry(String ver) {
        return dpsAryRepository.findByVer(ver);
    }

    @Override
    public List<DpsCell> getDpsCell(String ver) {
        return dpsCellRepository.findByVer(ver);
    }

    @Override
    public List<DpsLcm> getDpsLcm(String ver) {
        return dpsLcmRepository.findByVer(ver);
    }



    @Override
    public List<DpsAryOc> getDpsAryOc(String ver) {
        return dpsAryOcRepository.findByVer(ver);
    }

    @Override
    public List<DpsVer> getDpsVerByFileVer(String ver, String type) {
        return dpsVerRepository.findByDpsFileAndType(ver, type);
    }

    @Override
    public String generateDpsVer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = String.format("%03d", (dpsVerRepository.findAll().size()+1)%1000);
        return  sdf.format(new java.util.Date())+str;
    }

    @Override
    public void syncDpsLcm() {
        log.info("同步DPS LCM数据 >> START");
        List<String> verList = restService.getDpsLcmVer();
        if(verList == null || verList.size()==0) return;
        for(String ver : verList) {
            List list = getDpsVerByFileVer(ver, DpsVer.Type_Lcm);
            if(list == null || list.size() == 0) {
                syncDpsLcm(ver);
            }
        }
        log.info("同步DPS LCM数据 >> END");
    }

    @Override
    public void syncDpsLcm(String ver) {
        log.info("同步LCM DPS版本" + ver);
        List<Map> mapList = restService.getDpsLcm(ver);
        if(mapList==null || mapList.size() == 0) return;
        String fab = (String) mapList.get(0).get("fab_id");
        fab = fab.toUpperCase();
        String dps_ver = generateDpsVer();
        DpsVer dpsVer = new DpsVer();
        dpsVer.setFab(fab);
        dpsVer.setDpsFile(ver);
        dpsVer.setVer(dps_ver);
        dpsVer.setSource(DpsVer.Source_DPS);
        dpsVer.setType(DpsVer.Type_Lcm);
        dpsVer.setCreator("SYS");
        Date startDate = null;
        Date endDate = null;
        List<DpsLcm> dpsLcmList = new ArrayList<>();
        for(Map map : mapList) {
            String product = (String) map.get("prod_id");
            String project = (String) map.get("model_id");
            double demandQty = (Integer) map.get("bpc_qty");
            Date fabDate = (Date) map.get("fab_date");
            DpsLcm dpsLcm = new DpsLcm();
            dpsLcm.setVer(dps_ver);
            dpsLcm.setFab(fab);
            dpsLcm.setProduct(product);
            dpsLcm.setProject(project);
            dpsLcm.setCreator("SYS");
            dpsLcm.setFabDate(fabDate);
            dpsLcm.setDemandQty(demandQty);
            dpsLcm.setMemo("DPS同步");
            dpsLcmList.add(dpsLcm);

            //日期区间
            if(startDate == null || startDate.after(fabDate)) {
                startDate =fabDate;
            }
            if(endDate == null || endDate.before(fabDate)) {
                endDate = fabDate;
            }
        }
        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVerRepository.save(dpsVer);
        dpsLcmRepository.saveAll(dpsLcmList);
    }

    @Override
    public void syncDpsCell() {
        log.info("同步DPS CELL数据 >> START");
        List<String> verList = restService.getDpsCellAryVer();
        if(verList == null || verList.size()==0) return;
        for(String ver : verList) {
            List list = getDpsVerByFileVer(ver, DpsVer.Type_Cell);
            if(list == null || list.size() == 0) {
                syncDpsCell(ver);
            }
        }
        log.info("同步DPS CELL数据 >> END");
    }

    @Override
    public void syncDpsCell(String ver) {
        log.info("同步CELL DPS版本" + ver);
        List<Map> mapList = restService.getDpsCell(ver);
        if(mapList==null || mapList.size() == 0) return;
        String fab = "CELL";
        String dps_ver = generateDpsVer();
        DpsVer dpsVer = new DpsVer();
        dpsVer.setFab(fab);
        dpsVer.setDpsFile(ver);
        dpsVer.setVer(dps_ver);
        dpsVer.setSource(DpsVer.Source_Cell);
        dpsVer.setType(DpsVer.Type_Cell);
        dpsVer.setCreator("SYS");
        Date startDate = null;
        Date endDate = null;
        List<DpsCell> dpsCellList = new ArrayList<>();
        List<DpsCellOutputName> dpsCellOutputNameList = new ArrayList<>();
        String file_name = "";
        for(Map map : mapList) {
            String project = (String) map.get("model_id_dps");
            String outputName = (String) map.get("output_name");
            double demandQty = (Double) map.get("qty");
            Date fabDate = (Date) map.get("fab_date");
            file_name =  (String) map.get("file_name");
            DpsCell dpsCell = new DpsCell();
            dpsCell.setVer(dps_ver);
            dpsCell.setFab(fab);
            dpsCell.setProject(project);
            dpsCell.setOutputName(outputName);
            String product;
            if(StringUtils.contains(outputName, ",")) {
                product = outputName.substring(0, outputName.indexOf(","));

                DpsCellOutputName dpsCellOutputName = new DpsCellOutputName();
                dpsCellOutputName.setFab(fab);
                dpsCellOutputName.setVer(dps_ver);
                dpsCellOutputName.setOutputName(outputName);
                dpsCellOutputName.setProject(project);
                dpsCellOutputName.setFabDate(fabDate);
                dpsCellOutputName.setDemandQty(demandQty);
                dpsCellOutputNameList.add(dpsCellOutputName);
            } else {
                product = outputName;
            }
            dpsCell.setProduct(product);
            dpsCell.setCreator("SYS");
            dpsCell.setFabDate(fabDate);
            dpsCell.setDemandQty(demandQty);
            dpsCell.setMemo("DPS同步");
            dpsCellList.add(dpsCell);

            //日期区间
            if(startDate == null || startDate.after(fabDate)) {
                startDate =fabDate;
            }
            if(endDate == null || endDate.before(fabDate)) {
                endDate = fabDate;
            }

        }
        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVer.setFileName(file_name);
        dpsVerRepository.save(dpsVer);
        dpsCellRepository.saveAll(dpsCellList);
        dpsOutputNameService.saveDpsCellOutputName(dpsCellOutputNameList);
    }

    @Override
    public void syncDpsAry() {
        log.info("同步ARY DPS数据 >> START");
        List<String> verList = restService.getDpsCellAryVer();
        if(verList == null || verList.size()==0) return;
        for(String ver : verList) {
            List list = getDpsVerByFileVer(ver, DpsVer.Type_Ary);
            if(list == null || list.size() == 0) {
                syncDpsAry(ver);
            }
        }
        log.info("同步ARY DPS数据 >> END");
    }

    @Override
    public void syncDpsAry(String ver) {
        log.info("同步ARY DPS版本" + ver);
        List<Map> mapList = restService.getDpsAry(ver);
        if(mapList==null || mapList.size() == 0) return;
        String fab = "ARY";
        String dps_ver = generateDpsVer();
        DpsVer dpsVer = new DpsVer();
        dpsVer.setFab(fab);
        dpsVer.setDpsFile(ver);
        dpsVer.setVer(dps_ver);
        dpsVer.setSource(DpsVer.Source_Array);
        dpsVer.setType(DpsVer.Type_Ary);
        dpsVer.setCreator("SYS");
        Date startDate = null;
        Date endDate = null;
        String file_name = "";
        List<DpsAry> dpsAryArrayList = new ArrayList<>();
        for(Map map : mapList) {
            String project = (String) map.get("model_id_dps");
            String outputName = (String) map.get("output_name");
            double demandQty = (Double) map.get("qty");
            Date fabDate = (Date) map.get("fab_date");
            file_name =  (String) map.get("file_name");
            DpsAry dpsAry = new DpsAry();
            dpsAry.setProject(project);
            dpsAry.setOutputName(outputName);
            String product;
            if(StringUtils.contains(outputName, ",")) {
                product = outputName.substring(0, outputName.indexOf(","));
            } else {
                product = outputName;
            }
            dpsAry.setVer(dps_ver);
            dpsAry.setFab(fab);
            dpsAry.setProduct(product);
            dpsAry.setCreator("SYS");
            dpsAry.setFabDate(fabDate);
            dpsAry.setDemandQty(demandQty);
            dpsAry.setMemo("DPS同步");
            dpsAryArrayList.add(dpsAry);

            //日期区间
            if(startDate == null || startDate.after(fabDate)) {
                startDate =fabDate;
            }
            if(endDate == null || endDate.before(fabDate)) {
                endDate = fabDate;
            }
        }
        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVer.setFileName(file_name);
        dpsVerRepository.save(dpsVer);
        dpsAryRepository.saveAll(dpsAryArrayList);
        syncDpsAryOc(dps_ver, ver);
    }

    @Override
    public void syncDpsAryOc(String dps_ver, String fileVer) {
        log.info("同步ARY OC DPS版本" + fileVer);
        String fab = "ARY";
        List<Map> mapList = restService.getDpsAryOc(fileVer);
        List<DpsAryOc> dpsAryOcList = new ArrayList<>();
        for(Map map : mapList) {
            String project = (String) map.get("model_id_dps");
            String outputName = (String) map.get("output_name");
            double demandQty = (Double) map.get("qty");
            Date fabDate = (Date) map.get("fab_date");
            DpsAryOc dpsAryOc = new DpsAryOc();
            dpsAryOc.setVer(dps_ver);
            dpsAryOc.setFab(fab);
            dpsAryOc.setProject(project);
            dpsAryOc.setOutputName(outputName);
            String product;
            if(StringUtils.contains(outputName, ",")) {
                product = outputName.substring(0, outputName.indexOf(","));
            } else {
                product = outputName;
            }
            dpsAryOc.setProduct(product);
            dpsAryOc.setCreator("SYS");
            dpsAryOc.setFabDate(fabDate);
            dpsAryOc.setDemandQty(demandQty);
            dpsAryOc.setMemo("DPS同步");
            dpsAryOcList.add(dpsAryOc);
        }
        dpsAryOcRepository.saveAll(dpsAryOcList);
    }




    @Override
    public List<String> getDpsAryProduct(String ver) {
        return dpsAryRepository.getProduct(ver);
    }

    @Override
    public List<String> getDpsAryOcProduct(String ver) {
        return dpsAryOcRepository.getProduct(ver);
    }

    @Override
    public List<String> getDpsCellProduct(String ver) {
        return dpsCellRepository.getProduct(ver);
    }

    @Override
    public List<String> getDpsLcmProduct(String ver) {
        return dpsLcmRepository.getProduct(ver);
    }

    @Override
    public List<DpsAry> getDpsAry(String ver, String product) {
        return dpsAryRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public List<DpsAryOc> getDpsAryOc(String ver, String product) {
        return dpsAryOcRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public List<DpsCell> getDpsCell(String ver, String product) {
        return dpsCellRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public List<DpsLcm> getDpsLcm(String ver, String product) {
        return dpsLcmRepository.findByVerAndProduct(ver, product);
    }

    @Override
    public Page<DpsVer> queryDpsVer(int page, int limit, String searchFab, String searchType, String searchVer) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "ver");
        return dpsVerRepository.findByFabLikeAndTypeLikeAndVerLikeAndValidFlagIsTrue(searchFab+"%", searchType+"%",
                searchVer+"%", pageable);
    }

    @Override
    public List getDpsDate(String ver) {
        DpsVer dpsVer = getDpsVer(ver);
        if(dpsVer == null) return new ArrayList();
        String type = dpsVer.getType();
        List list;
        switch (type) {
            case DpsVer.Type_Ary:
                list = getDpsAry(ver);
                break;
            case DpsVer.Type_Cell:
                list = getDpsCell(ver);
                break;
            case DpsVer.Type_Lcm:
                list = getDpsLcm(ver);
                break;
            default:
                list = new ArrayList();
        }
        return list;
    }

    @Override
    public List<Date> getDpsCalendar(String ver) {
        DpsVer dpsVer = getDpsVer(ver);
        if(dpsVer == null) return new ArrayList<>();
        List<java.util.Date> dateList = DateUtil.getCalendar(dpsVer.getStartDate(), dpsVer.getEndDate());
        List<Date> dates = new ArrayList<>();
        for(java.util.Date d : dateList) {
            dates.add(new Date(d.getTime()));
        }
        return dates;
    }

    @Override
    public List getProductDpsData(String ver, String searchProduct) {
        DpsVer dpsVer = getDpsVer(ver);

        return null;
    }

    @Override
    public Page getPageProduct(String ver, int page, int limit, String searchProduct) {
        DpsVer dpsVer = getDpsVer(ver);
        String type = dpsVer.getType();
        Page p;
        switch (type) {
            case DpsVer.Type_Ary:
                p = getPagDpsAryProduct(ver, page, limit, searchProduct);
                break;
            case DpsVer.Type_Cell:
                p = getPagDpsCellProduct(ver, page, limit, searchProduct);
                break;
            case DpsVer.Type_Lcm:
                p = getPagDpsLcmProduct(ver, page, limit, searchProduct);
                break;
            default:
                p = null;
        }
        return p;
    }

    @Override
    public Page getPagDpsAryProduct(String ver, int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return dpsAryRepository.getPageProduct(ver, searchProduct+"%", pageable);
    }

    @Override
    public Page getPagDpsAryOcProduct(String ver, int page, int limit, String searchProduct) {
        return null;
    }

    @Override
    public Page getPagDpsCellProduct(String ver, int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return dpsCellRepository.getPageProduct(ver, searchProduct+"%", pageable);
    }

    @Override
    public Page getPagDpsLcmProduct(String ver, int page, int limit, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        return dpsLcmRepository.getPageProduct(ver, searchProduct+"%", pageable);
    }


    @Override
    public List<DpsLcm> getDpsLcm(String ver, String product, Date startDate) {
        return dpsLcmRepository.findByVerAndProductAndFabDateGreaterThanEqual(ver, product, startDate);
    }

    @Override
    public List<DpsAry> getDpsAry(String ver, String product, Date startDate) {
        return dpsAryRepository.findByVerAndProductAndFabDateGreaterThanEqual(ver, product, startDate);
    }

    @Override
    public List<DpsAryOc> getDpsAryOc(String ver, String product, Date startDate) {
        return dpsAryOcRepository.findByVerAndProductAndFabDateGreaterThanEqual(ver, product, startDate);
    }

    @Override
    public List<DpsCell> getDpsCell(String ver, String product, Date startDate) {
        return dpsCellRepository.findByVerAndProductAndFabDateGreaterThanEqual(ver, product, startDate);
    }

    @Override
    public String importLcmDps(InputStream inputStream, String fileName) {
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        if(list == null || list.size() == 0) {
            throw new RuntimeException("excel无数据");
        }
        //生成DPS版本
        String ver = generateDpsVer();

        //第一行厂别
        int rowInt = 0;int colInt = 0;
        String fab = (String) list.get(rowInt).get(colInt);
        if(StringUtils.containsIgnoreCase(fab, "LCM1")) {
            fab = "LCM1";
        } else if(StringUtils.containsIgnoreCase(fab, "LCM2")) {
            fab = "LCM2";
        }
        rowInt++;

        String[] propertyTypes = new String[] {"model","product"};
        Map<String, Integer> propertyTypesMap = new HashMap<>();
        for(int i=0; i<propertyTypes.length; i++) {
            propertyTypesMap.put(propertyTypes[i], i);
        }
        Date[] dates = {};
        List<DpsLcm> dpsDataList = new ArrayList<>();
        try {
            // 解析第二行
            List<Object> firstRow = list.get(rowInt);
            // 日期数组
            dates = new Date[firstRow.size()];
            for(;colInt<firstRow.size(); colInt++) {
                if(colInt<propertyTypes.length) {
                    continue;
                }
                Object object = firstRow.get(colInt);
                if(object instanceof String) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateStr = (String) object;
                    dateStr =  dateStr.trim().replace("/", "-");
                    dates[colInt] = new Date(sdf.parse(dateStr).getTime());
                } else if(object instanceof BigDecimal) {
                    BigDecimal b = (BigDecimal) object;
                    int days = b.intValue();//天数
                    //获取时间
                    Calendar c = Calendar.getInstance();
                    c.set(1900, 0, 1);
                    c.add(Calendar.DATE, days - 2);
                    dates[colInt] = new Date(c.getTime().getTime());
                } else {
                    dates[colInt] = new Date(((java.util.Date) object).getTime());
                }
            }
            colInt = 0;
            rowInt++;

            // 解析后面数据
            String[] propertyValues = new String[propertyTypes.length];
            for(;rowInt<list.size(); rowInt++) {
                List row = list.get(rowInt);
                // 前面列
                for(; colInt<propertyTypes.length; colInt++) {
                    Object obj = row.get(colInt);
                    String value;
                    if(obj instanceof String) {
                        value = (String) obj;
                    } else if(obj != null) {
                        value = obj.toString();
                    } else {
                        value = "";
                    }
                    if(StringUtils.isNoneEmpty(value)) {
                        propertyValues[colInt] = value;
                    }
                }
                // 后面列
                for(; colInt<row.size(); colInt++) {
                    Object value = row.get(colInt);
                    if(value == null) {
                        continue;
                    }
                    double qty;
                    if(value instanceof String) {
                        if(StringUtils.isEmpty((String)value)) {
                            continue;
                        }
                        qty = Double.parseDouble((String) value);
                    } else {
                        qty = ((BigDecimal) value).doubleValue();
                    }
                    if(qty==0) {
                        continue;
                    }

                    Date fabeDate = dates[colInt];
                    DpsLcm dpsData = new DpsLcm();
                    dpsData.setFab(fab);
                    dpsData.setVer(ver);
                    dpsData.setProduct(propertyValues[propertyTypesMap.get("product")]);
                    dpsData.setFabDate(fabeDate);
                    dpsData.setDemandQty(qty);
                    if(propertyTypesMap.get("model") != null) {
                        dpsData.setProject(propertyValues[propertyTypesMap.get("model")]);
                    }
                    dpsData.setMemo("由MC手动导入");
                    dpsDataList.add(dpsData);

                }
                colInt = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列");
            String msg = "上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列";
            throw new RuntimeException(msg, e);
        }

        DpsVer dpsVer = new DpsVer();
        dpsVer.setVer(ver);
        dpsVer.setFab(fab);
        dpsVer.setStartDate(dates[propertyTypes.length]);
        dpsVer.setEndDate(dates[dates.length-1]);
        dpsVer.setSource("MC导入");
        dpsVer.setType(DpsVer.Type_Lcm);

        //保存
        dpsVerRepository.save(dpsVer);
        dpsLcmRepository.saveAll(dpsDataList);
        return ver;
    }

    @Override
    public List<DpsCell> getDpsCellByOutputName(String ver, String outputName) {
        return dpsCellRepository.findByVerAndOutputName(ver, outputName);
    }

    @Override
    public List<DpsAry> getDpsAryByOutputName(String ver, String outputName) {
        return null;
    }

    @Override
    public void deleteDpsCell(List<DpsCell> dpsCellList) {
        dpsCellRepository.deleteAll(dpsCellList);
    }

    @Override
    public void saveDpsCell(List<DpsCell> list) {
        dpsCellRepository.saveAll(list);
    }

    @Override
    public void saveDpsVer(DpsVer dpsVer) {
        dpsVerRepository.save(dpsVer);
    }
}
