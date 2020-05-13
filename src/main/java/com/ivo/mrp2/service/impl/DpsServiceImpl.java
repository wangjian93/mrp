package com.ivo.mrp2.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp2.entity.Dps;
import com.ivo.mrp2.repository.DpsRepository;
import com.ivo.mrp2.service.DpsService;
import com.ivo.rest.dps.service.RestDpsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    public DpsServiceImpl(DpsRepository dpsRepository, RestDpsService restDpsService) {
        this.dpsRepository = dpsRepository;
        this.restDpsService = restDpsService;
    }

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
    public List<Dps> getDps(String ver) {
        return dpsRepository.findByDpsVer(ver);
    }

    @Override
    public List<String> getDpsVer() {
        return dpsRepository.getDpsVer();
    }

    @Override
    public List<Date> getDpsCalendarList(String dpsVer) {
        Date[] dates = getDpsDateRange(dpsVer);
        List<java.util.Date> list = DateUtil.getCalendar(dates[0], dates[1]);
        List<Date> sqlDateList = new ArrayList<>();
        if(list == null) return sqlDateList;
        for(java.util.Date date : list) {
            sqlDateList.add(new Date(date.getTime()));
        }
        return sqlDateList;
    }

    @Override
    public List<Dps> getDps(String ver, String product) {
        return dpsRepository.findByDpsVerAndProduct(ver, product);
    }

    @Override
    public String importDps(InputStream inputStream, String fileName) {
        String dpsVer = "MC"+System.currentTimeMillis();
        String fab;
        String[] propertyTypes;
        if(StringUtils.containsIgnoreCase(fileName, "LCM1")) {
            fab = "LCM1";
            dpsVer += fab;
            propertyTypes = new String[] {"model","product"};
        } else if(StringUtils.containsIgnoreCase(fileName, "LCM2")) {
            fab = "LCM2";
            dpsVer += fab;
            propertyTypes = new String[] {"model","product"};
        } else {
            fab = "CELL";
            dpsVer += fab;
            propertyTypes = new String[] {"product"};
        }

        Map<String, Integer> propertyTypesMap = new HashMap<>();
        for(int i=0; i<propertyTypes.length; i++) {
            propertyTypesMap.put(propertyTypes[i], i);
        }

        List<List<Object>> list;
        try {
            if (fileName.endsWith("xls")) {
                list =  ExcelUtil.readXlsFirstSheet(inputStream);
            }
            else if (fileName.endsWith("xlsx")) {
                list =  ExcelUtil.readXlsxFirstSheet(inputStream);
            }
            else {
                throw new IOException("文件类型错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("excel读取失败");
            throw new RuntimeException("excel读取失败," + e.getMessage(), e);
        }
        if(list == null || list.size() == 0) {
            throw new RuntimeException("excel无数据");
        }

        int rowInt = 0;
        int colInt = 0;
        try {
            // 解析第一行
            List<Object> firstRow = list.get(0);
            // 日期数组
            java.util.Date[] dates = new java.util.Date[firstRow.size()];
            for(;colInt<firstRow.size(); colInt++) {
                if(colInt<propertyTypes.length) {
                    continue;
                }
                Object object = firstRow.get(colInt);
                if(object instanceof String) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
                    String dateStr = (String) object;
                    dateStr =  dateStr.trim().replace("/", "-");
                    dates[colInt] = sdf.parse(dateStr);
                } else if(object instanceof BigDecimal) {
                    BigDecimal b = (BigDecimal) object;
                    int days = b.intValue();//天数
                    //获取时间
                    Calendar c = Calendar.getInstance();
                    c.set(1900, 0, 1);
                    c.add(Calendar.DATE, days - 2);
                    dates[colInt] = c.getTime();
                } else {
                    dates[colInt] = (java.util.Date) object;
                }
            }
            colInt = 0;
            rowInt++;

            // 解析后面数据
            String[] propertyValues = new String[propertyTypes.length];
            List<Dps> dpsList = new ArrayList<>();
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

                    Date fabeDate = new Date(dates[colInt].getTime());
                    Dps dps = new Dps();
                    dps.setDpsVer(dpsVer);
                    dps.setFab(fab);
                    dps.setQty(qty);
                    dps.setFabDate(fabeDate);
                    dps.setProduct(propertyValues[propertyTypesMap.get("product")]);
                    if(propertyTypesMap.get("model") != null) {
                        dps.setModel(propertyValues[propertyTypesMap.get("model")]);
                    }
                    dpsList.add(dps);
                }
                colInt = 0;
            }
            dpsRepository.saveAll(dpsList);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列");
            String msg = "上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列";
            throw new RuntimeException(msg, e);
        }
        return dpsVer;
    }

    @Override
    public String getPlantByDpsVer(String dpsVer) {
        return dpsRepository.findTopByDpsVer(dpsVer).getFab();
    }

    @Override
    public List<String> getProduct(String dpsVer) {
        return dpsRepository.getProduct(dpsVer);
    }
}
