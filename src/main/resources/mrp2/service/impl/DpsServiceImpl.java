package com.ivo.mrp2.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp2.entity.Dps;
import com.ivo.mrp2.entity.DpsData;
import com.ivo.mrp2.entity.DpsVer;
import com.ivo.mrp2.key.DpsPrimaryKey;
import com.ivo.mrp2.repository.DpsDataRepository;
import com.ivo.mrp2.repository.DpsRepository;
import com.ivo.mrp2.repository.DpsVerRepository;
import com.ivo.mrp2.service.DpsService;
import com.ivo.rest.cellDps.service.RestCellDpsService;
import com.ivo.rest.dps.entity.RestDps;
import com.ivo.rest.dps.service.RestDpsService;
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

    private DpsRepository dpsRepository;

    private RestDpsService restDpsService;

    private RestCellDpsService restCellDpsService;

    @Override
    public void syncDps() {
        log.info("START>> 同步DPS");
        List<String> verList = restDpsService.getDpsVer();
        for(String ver : verList) {
            log.info("版本" + ver + "...");
            if(isExistVer(ver)) continue;
            List<Dps> dpsList = restDpsService.getDpsByVer2(ver);
            dpsRepository.saveAll(dpsList);
        }
        log.info("END>> 同步DPS");
    }

    @Override
    public boolean isExistVer(String ver) {
        return dpsRepository.findTopByDpsVer(ver) != null;
    }

    @Override
    public Date[] getDpsDateRange(String ver) {
        Date[] dates = {null, null};
        Map<String, Date> map = dpsRepository.getDpsDateRange(ver);
        if(map.get("startDate") != null) {
            dates[0] = map.get("startDate");
        }
        if(map.get("endDate") != null) {
            dates[1] = map.get("endDate");
        }
        return dates;
    }

    @Override
    public List<String> getDpsVer() {
        return dpsRepository.getDpsVer();
    }

    @Override
    public List<Dps> getDps(String ver, String product) {
        return dpsRepository.findByDpsVerAndProduct(ver, product);
    }

    @Override
    public String getPlantByDpsVer(String dpsVer) {
        return dpsRepository.findTopByDpsVer(dpsVer).getFab();
    }



//    @Override
//    public List<Map> summaryMaterial(String dpsVer) {
//        return dpsRepository.summaryMaterial(dpsVer);
//    }


    private DpsVerRepository dpsVerRepository;

    private DpsDataRepository dpsDataRepository;

    @Autowired
    public DpsServiceImpl(DpsRepository dpsRepository, RestDpsService restDpsService, RestCellDpsService restCellDpsService,
                          DpsVerRepository dpsVerRepository, DpsDataRepository dpsDataRepository) {
        this.dpsRepository = dpsRepository;
        this.restDpsService = restDpsService;
        this.dpsVerRepository = dpsVerRepository;
        this.dpsDataRepository = dpsDataRepository;
        this.restCellDpsService = restCellDpsService;
    }

    @Override
    public void sync() {
        // 同步LCM
        List<String> verList = restDpsService.getDpsVer();
        if(verList!=null && verList.size()>0) {
            for(String ver : verList) {
                //版本不存在时同步
                if(getDpsVer(ver)==null) {
                    syncLcmDps(ver);
                }
            }
        }
        // 同步CELL
        List<String> cellDpsVer = restCellDpsService.getCellDpsVer();
        if(cellDpsVer!=null && cellDpsVer.size()>0) {
            for(String ver : cellDpsVer) {
                //版本不存在时同步
                if(getDpsVer(ver+"CELL")==null) {
                    syncCellDps(ver);
                }
                if(getDpsVer(ver+"ARY")==null) {
                    syncArrayDps(ver);
                }
            }
        }
    }

    @Override
    public void syncLcmDps(String ver) {
        log.info("同步LCM的DPS>> START");
        List<RestDps> dpsList = restDpsService.getDpsByVer(ver);
        if(dpsList==null || dpsList.size()==0) return;

        String fab = dpsList.get(0).getFab_id();
        Date startDate = null;
        Date endDate = null;
        List<DpsData> dpsDataList = new ArrayList<>();
        Set<String> productSet = new HashSet<>();
        String memo = "DPS同步";
        for(RestDps d : dpsList) {
            DpsData dpsData = new DpsData();
            dpsData.setDpsVer(ver);
            dpsData.setProduct(d.getProd_id());
            dpsData.setFabDate(d.getFab_date());
            dpsData.setQty(d.getBpc_qty());
            dpsData.setModel(d.getModel_id());
            dpsData.setMemo(memo);
            dpsDataList.add(dpsData);

            if(startDate == null || startDate.after(dpsData.getFabDate())) {
                startDate = dpsData.getFabDate();
            }
            if(endDate == null || endDate.before(dpsData.getFabDate())) {
                endDate = dpsData.getFabDate();
            }

            productSet.add(dpsData.getProduct());
        }

        //当月第一天
        startDate = new Date(DateUtil.getMonthEveryDays(startDate).get(0).getTime());
        //当月最后一天
        List<java.util.Date> list = DateUtil.getMonthEveryDays(endDate);
        endDate = new Date(list.get(list.size()-1).getTime());
        DpsVer dpsVer = new DpsVer();
        dpsVer.setVer(ver);
        dpsVer.setFab(fab);
        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVer.setSource(memo);
        dpsVer.setType(DpsVer.TYPE_DPS);
        dpsVer.setProductQty(productSet.size());
        dpsVer.setDemandQty(dpsDataList.size());

        dpsVerRepository.save(dpsVer);
        dpsDataRepository.saveAll(dpsDataList);
        log.info("同步LCM的DPS>> END");
    }

    @Override
    public void syncCellDps(String ver) {
        log.info("同步CELL的DPS>> START");
        List<Map> dpsList = restCellDpsService.getCellDpsData(ver);
        if(dpsList==null || dpsList.size()==0) return;
        String ver_ = ver+"CELL";
        String fab = "CELL";
        Date startDate = null;
        Date endDate = null;
        List<DpsData> dpsDataList = new ArrayList<>();
        Set<String> productSet = new HashSet<>();
        String memo = "EXCEL解析Cell Input";
        for(Map d : dpsList) {
            DpsData dpsData = new DpsData();
            Date fabDate = (Date) d.get("fabDate");
            double qty = (Double)d.get("qty");
            dpsData.setDpsVer(ver_);
            dpsData.setProduct((String)d.get("product"));
            dpsData.setFabDate(fabDate);
            dpsData.setQty(qty);
            dpsData.setMemo(memo);
            dpsDataList.add(dpsData);

            if(startDate == null || startDate.after(dpsData.getFabDate())) {
                startDate = dpsData.getFabDate();
            }
            if(endDate == null || endDate.before(dpsData.getFabDate())) {
                endDate = dpsData.getFabDate();
            }

            productSet.add(dpsData.getProduct());
        }
        //当月第一天
        startDate = new Date(DateUtil.getMonthEveryDays(startDate).get(0).getTime());
        //当月最后一天
        List<java.util.Date> list = DateUtil.getMonthEveryDays(endDate);
        endDate = new Date(list.get(list.size()-1).getTime());
        DpsVer dpsVer = new DpsVer();
        dpsVer.setVer(ver_);
        dpsVer.setFab(fab);
        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVer.setSource(memo);
        dpsVer.setType(DpsVer.TYPE_DPS);
        dpsVer.setProductQty(productSet.size());
        dpsVer.setDemandQty(dpsDataList.size());

        dpsVerRepository.save(dpsVer);
        dpsDataRepository.saveAll(dpsDataList);
        log.info("同步CELL的DPS>> END");
    }

    @Override
    public void syncArrayDps(String ver) {
        log.info("同步Array的DPS>> START");
        List<Map> dpsList = restCellDpsService.getArrayDpsData(ver);
        if(dpsList==null || dpsList.size()==0) return;

        String ver_ = ver+"ARY";
        String fab = "ARY";
        Date startDate = null;
        Date endDate = null;
        List<DpsData> dpsDataList = new ArrayList<>();
        Set<String> productSet = new HashSet<>();
        String memo = "EXCEL解析Array Input";
        for(Map d : dpsList) {
            DpsData dpsData = new DpsData();
            Date fabDate = (Date) d.get("fabDate");
            double qty = (Double)d.get("qty");
            dpsData.setDpsVer(ver_);
            dpsData.setProduct((String)d.get("product"));
            dpsData.setFabDate(fabDate);
            dpsData.setQty(qty);
            dpsData.setMemo(memo);
            dpsDataList.add(dpsData);

            if(startDate == null || startDate.after(dpsData.getFabDate())) {
                startDate = dpsData.getFabDate();
            }
            if(endDate == null || endDate.before(dpsData.getFabDate())) {
                endDate = dpsData.getFabDate();
            }

            productSet.add(dpsData.getProduct());
        }
        //当月第一天
        startDate = new Date(DateUtil.getMonthEveryDays(startDate).get(0).getTime());
        //当月最后一天
        List<java.util.Date> list = DateUtil.getMonthEveryDays(endDate);
        endDate = new Date(list.get(list.size()-1).getTime());
        DpsVer dpsVer = new DpsVer();
        dpsVer.setVer(ver_);
        dpsVer.setFab(fab);
        dpsVer.setStartDate(startDate);
        dpsVer.setEndDate(endDate);
        dpsVer.setSource(memo);
        dpsVer.setType(DpsVer.TYPE_DPS);
        dpsVer.setProductQty(productSet.size());
        dpsVer.setDemandQty(dpsDataList.size());

        dpsVerRepository.save(dpsVer);
        dpsDataRepository.saveAll(dpsDataList);
        log.info("同步Array的DPS>> END");
    }

    @Override
    public String importDps(InputStream inputStream, String fileName) {
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
        } else {
            fab = "CELL";
        }
        rowInt++;

        String[] propertyTypes = new String[] {"model","product"};
        Map<String, Integer> propertyTypesMap = new HashMap<>();
        for(int i=0; i<propertyTypes.length; i++) {
            propertyTypesMap.put(propertyTypes[i], i);
        }
        Date[] dates = {};
        List<DpsData> dpsDataList = new ArrayList<>();
        Set<String> productSet = new HashSet<>();
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
                    DpsData dpsData = new DpsData();
                    dpsData.setDpsVer(ver);
                    dpsData.setProduct(propertyValues[propertyTypesMap.get("product")]);
                    dpsData.setFabDate(fabeDate);
                    dpsData.setQty(qty);
                    if(propertyTypesMap.get("model") != null) {
                        dpsData.setModel(propertyValues[propertyTypesMap.get("model")]);
                    }
                    dpsData.setMemo("MC导入");
                    dpsDataList.add(dpsData);

                    productSet.add(dpsData.getProduct());
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
        dpsVer.setType(DpsVer.TYPE_DPS);
        dpsVer.setProductQty(productSet.size());
        dpsVer.setDemandQty(dpsDataList.size());
        //保存
        dpsVerRepository.save(dpsVer);
        dpsDataRepository.saveAll(dpsDataList);
        return ver;
    }

    @Override
    public DpsVer getDpsVer(String ver) {
        return dpsVerRepository.findById(ver).orElse(null);
    }

    @Override
    public String generateDpsVer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return  sdf.format(new java.util.Date()) +  "MC";
    }

    @Override
    public void exportDps(String ver) {

    }

    @Override
    public Page<DpsVer> getDpsVer(int page, int limit) {
        Sort sort = new Sort(Sort.Direction.DESC, "ver");
        Pageable pageable = PageRequest.of(page, limit, sort);
        return dpsVerRepository.findAll(pageable);
    }

    @Override
    public List<DpsData> getDpsData(String ver) {
        return dpsDataRepository.findByDpsVer(ver);
    }

    @Override
    public List<DpsData> getDpsData(String ver, String product) {
        return dpsDataRepository.findByDpsVerAndProduct(ver, product);
    }

    @Override
    public List<Date> getDpsCalendarList(String ver) {
        DpsVer dpsVer = getDpsVer(ver);
        List<java.util.Date> list = DateUtil.getCalendar(dpsVer.getStartDate(), dpsVer.getEndDate());
        List<Date> sqlDateList = new ArrayList<>();
        if(list == null) return sqlDateList;
        for(java.util.Date date : list) {
            sqlDateList.add(new Date(date.getTime()));
        }
        return sqlDateList;
    }

    @Override
    public void resizeQty(String ver, String product, Date fabDate, double resizeQty) {
        log.info("调整DPS数量>> START");
        DpsVer dpsVer = getDpsVer(ver);
        //小版本
        String smallVer = dpsVer.getSmallVer();
        if(StringUtils.isEmpty(smallVer)) {
            smallVer = dpsVer.getVer()+"S";
            dpsVer.setSmallVer(smallVer);
            dpsVerRepository.save(dpsVer);
        }
        DpsData dpsData = getDpsDate(smallVer, product, fabDate);
        if(dpsData==null) {
            dpsData = new DpsData();
            dpsData.setDpsVer(smallVer);
            dpsData.setProduct(product);
            dpsData.setFabDate(fabDate);
        }
        dpsData.setQty(resizeQty);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dpsData.setMemo(sdf.format(new java.util.Date())+"调整数量");
        dpsDataRepository.save(dpsData);
        log.info("调整DPS数量>> END");
    }

    @Override
    public DpsData getDpsDate(String ver, String product, Date fabDate) {
        DpsPrimaryKey dpsPrimaryKey = new DpsPrimaryKey(ver, product, fabDate);
        return dpsDataRepository.findById(dpsPrimaryKey).orElse(null);
    }

    @Override
    public void cancelResize(String ver, String product, Date fabDate) {
        log.info("取消DPS调整>> START");
        DpsVer dpsVer = getDpsVer(ver);
        DpsData dpsData = getDpsDate(dpsVer.getSmallVer(), product, fabDate);
        if(dpsData == null) return;
        dpsDataRepository.delete(dpsData);
        if(getDpsData(dpsVer.getSmallVer()).size()==0) {
            dpsVer.setSmallVer(null);
            dpsVerRepository.save(dpsVer);
        }
        log.info("取消DPS调整>> END");
    }

    @Override
    public List<String> getProduct(String dpsVer) {
        return dpsDataRepository.getProduct(dpsVer);
    }
}
