package com.ivo.mrp2.service.impl;

import com.ivo.common.utils.ExcelUtil;
import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.key.SupplierArrivalPlanKey;
import com.ivo.mrp2.repository.SupplierArrivalPlanRepository;
import com.ivo.mrp2.service.*;
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
public class SupplierArrivalPlanServiceImpl implements SupplierArrivalPlanService {

    private SupplierArrivalPlanRepository arrivalPlanRepository;



    private MaterialService materialService;

    private SupplierService supplierService;

    @Autowired
    public SupplierArrivalPlanServiceImpl(SupplierArrivalPlanRepository arrivalPlanRepository,
                                          MaterialService materialService,
                                          SupplierService supplierService) {
        this.arrivalPlanRepository = arrivalPlanRepository;
        this.materialService = materialService;
        this.supplierService = supplierService;
    }

    @Override
    public double getMaterialArrivalPlanQty(String plant, String material, Date date) {
        Double d = arrivalPlanRepository.getMaterialArrivalPlanQty(plant, material, date);
        return d==null ? 0 : d;
    }

    @Override
    public List<SupplierArrivalPlan> getSupplierArrivalPlan(String plant, String material, Date date) {
        return arrivalPlanRepository.findByPlantAndMaterialAndDate(plant, material, date);
    }

    @Override
    public void saveSupplierArrivalPlan(List<SupplierArrivalPlan> supplierArrivalPlans) {
        arrivalPlanRepository.saveAll(supplierArrivalPlans);
    }

    @Override
    public List<Map> getSupplierArrivalQty(Date startDate, Date endDate, String plant, String material, String supplierCode) {
        return arrivalPlanRepository.getSupplierArrivalQty(startDate, endDate, plant, material, supplierCode);
    }

    @Override
    public List<Map> getDaySupplierArrivalQty(Date startDate, Date endDate, String plant, String material) {
        return arrivalPlanRepository.getDaySupplierArrivalQty(startDate, endDate, plant,  material);
    }



    @Override
    public Page<Map> getPageSupplierArrivalPlan(int page, int limit, String plant, String material,
                                                String supplier, Date startDate, Date endDate) {
        plant = plant + '%';
        material = material + '%';
        supplier = supplier + '%';
        Pageable pageable = PageRequest.of(page-1, limit);
        return arrivalPlanRepository.pageQuerySupplierArrivalPlan(startDate, endDate, plant, material, supplier, pageable);
    }

    @Override
    public SupplierArrivalPlan getSupplierArrivalPlan(String plant, String material, String supplier, Date date) {
        SupplierArrivalPlanKey key = new SupplierArrivalPlanKey(plant, date, material, supplier);
        return arrivalPlanRepository.findById(key).orElse(null);
    }

    @Override
    public void importArrivalPlan(InputStream inputStream, String fileName) {
        List<List<Object>> list = ExcelUtil.readExcelFirstSheet(inputStream, fileName);
        if(list == null || list.size() == 0) {
            throw new RuntimeException("excel无数据");
        }

        String[] propertyTypes = new String[] {"plant","material","supplierCode"};
        Map<String, Integer> propertyTypesMap = new HashMap<>();
        for(int i=0; i<propertyTypes.length; i++) {
            propertyTypesMap.put(propertyTypes[i], i);
        }

        int rowInt = 0;
        int colInt = 0;
        List<SupplierArrivalPlan> supplierArrivalPlanList = new ArrayList<>();
        try {
            // 解析第一行
            List<Object> firstRow = list.get(rowInt);
            // 日期数组
            java.util.Date[] dates = new java.util.Date[firstRow.size()];
            for(;colInt<firstRow.size(); colInt++) {
                if(colInt<propertyTypes.length) {
                    continue;
                }
                Object object = firstRow.get(colInt);
                if(object instanceof String) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
                    SupplierArrivalPlan supplierArrivalPlan = new SupplierArrivalPlan();
                    String plant = propertyValues[propertyTypesMap.get("plant")];
                    String material = propertyValues[propertyTypesMap.get("material")];
                    String supplierCode = propertyValues[propertyTypesMap.get("supplierCode")];
                    supplierArrivalPlan.setPlant(plant);
                    supplierArrivalPlan.setMaterial(material);
                    supplierArrivalPlan.setMaterialGroup(materialService.getMaterialGroup(material));
                    supplierArrivalPlan.setSupplier(supplierCode);
                    supplierArrivalPlan.setMaterialName(materialService.getMaterialName(material));
                    supplierArrivalPlan.setDate(fabeDate);
                    supplierArrivalPlan.setArrivalQty(qty);
                    supplierArrivalPlan.setCreateDate(new java.util.Date());
                    supplierArrivalPlan.setMemo("EXCEL上传");
                    supplierArrivalPlanList.add(supplierArrivalPlan);
                }
                colInt = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列");
            String msg = "上传失败，Excel格式不准确，第"+rowInt+"行第"+colInt+"列";
            throw new RuntimeException(msg, e);
        }
        arrivalPlanRepository.saveAll(supplierArrivalPlanList);
    }

    @Override
    public void saveSupplierArrivalPlan(SupplierArrivalPlan supplierArrivalPlan) {
        arrivalPlanRepository.save(supplierArrivalPlan);
    }

    @Override
    public void saveSupplierArrivalPlan(String plant, String material, String supplier, Date fabDate, double arrivalQty) {
        SupplierArrivalPlan supplierArrivalPlan = getSupplierArrivalPlan(plant, material, supplier, fabDate);
        if(supplierArrivalPlan == null) {
            if(arrivalQty == 0) return;
            supplierArrivalPlan = new SupplierArrivalPlan();
            supplierArrivalPlan.setPlant(plant);
            supplierArrivalPlan.setMaterial(material);
            supplierArrivalPlan.setMaterialName(materialService.getMaterialName(material));
            supplierArrivalPlan.setMaterialGroup(materialService.getMaterialGroup(material));
            supplierArrivalPlan.setSupplier(supplier);
            supplierArrivalPlan.setSupplierName(supplierService.getSupplier(supplier).getSName());
            supplierArrivalPlan.setDate(fabDate);
            supplierArrivalPlan.setCreateDate(new java.util.Date());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            supplierArrivalPlan.setMemo(sdf.format(new java.util.Date()) + "添加");
            supplierArrivalPlan.setArrivalQty(arrivalQty);
            saveSupplierArrivalPlan(supplierArrivalPlan);
        } else {
            if(arrivalQty != supplierArrivalPlan.getArrivalQty()) {
                supplierArrivalPlan.setArrivalQty(arrivalQty);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                supplierArrivalPlan.setMemo(sdf.format(new java.util.Date()) + "修改");
                saveSupplierArrivalPlan(supplierArrivalPlan);
            }
        }
    }

    @Override
    public List<Map> getAllocationArrivalPlan(Date startDate, Date endDate, String plant, String material, String supplier) {
        return arrivalPlanRepository.getAllocationArrivalPlan(startDate, endDate, plant+"%", material+"%", supplier+"%");
    }

    @Override
    public void saveAll(List<SupplierArrivalPlan> supplierArrivalPlans) {
        arrivalPlanRepository.saveAll(supplierArrivalPlans);
    }

    @Override
    public void deleteAll(List<SupplierArrivalPlan> supplierArrivalPlanList) {
        arrivalPlanRepository.deleteAll(supplierArrivalPlanList);
    }
}
