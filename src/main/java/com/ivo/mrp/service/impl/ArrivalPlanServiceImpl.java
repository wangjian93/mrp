package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.key.ArrivalPlanKey;
import com.ivo.mrp.repository.ArrivalPlanRepository;
import com.ivo.mrp.service.ArrivalPlanService;
import com.ivo.mrp.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class ArrivalPlanServiceImpl implements ArrivalPlanService {


    private ArrivalPlanRepository arrivalPlanRepository;

    private SupplierService supplierService;

    @Autowired
    public ArrivalPlanServiceImpl(ArrivalPlanRepository arrivalPlanRepository, SupplierService supplierService) {
        this.arrivalPlanRepository = arrivalPlanRepository;
        this.supplierService = supplierService;
    }

    @Override
    public double getArrivalPlanQty(String fab, String material, Date fabDate) {
        Double d = arrivalPlanRepository.getArrivalPlanQty(fab, material, fabDate);
        return d == null ? 0 : d;
    }

    @Override
    public List<ArrivalPlan> getArrivalPlan(String fab, String material, Date fabDate) {
        return arrivalPlanRepository.findByFabAndMaterialAndFabDate(fab, material, fabDate);
    }

    @Override
    public Map<Date, Double> getArrivalPlanQty(String fab, String material, List<Date> fabDateList) {
        List<Map> mapList = arrivalPlanRepository.getArrivalPlan(fab, material, fabDateList);
        HashMap<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double allocationQty = (Double) map.get("arrivalQty");
            m.put(fabDae, allocationQty);
        }
        return m;
    }

    @Override
    public double getArrivalPlanPackage(String fab, String product, String type, Double linkQty, String mode, String material, Date fabDate) {
        return 0;
    }

    @Override
    public void batchSaveArrivalPlan(List<Map> mapList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map map : mapList) {
            String material = (String) map.get("material");
            String fabDate_ = (String) map.get("fabDate");
            String fab = (String) map.get("fab");
            String supplierCode = (String) map.get("supplierCode");
            double arrivalQty = Double.parseDouble( String.valueOf(map.get("arrivalQty")) );
            Date fabDate = null;
            try {
                fabDate = new Date(sdf.parse(fabDate_).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ArrivalPlan arrivalPlan = arrivalPlanRepository.findById(new ArrivalPlanKey(fab, material, fabDate, supplierCode)).orElse(null);
            if(arrivalPlan == null) {
                if(arrivalQty == 0) continue;
                arrivalPlan = new ArrivalPlan();
                arrivalPlan.setFab(fab);
                arrivalPlan.setMaterial(material);
                arrivalPlan.setFabDate(fabDate);
                arrivalPlan.setSupplierCode(supplierCode);
                arrivalPlan.setArrivalQty(arrivalQty);

                Supplier supplier = supplierService.getSupplier(supplierCode);
                if(supplier != null) {
                    arrivalPlan.setSupplierSname(supplier.getSupplierSname());
                }
            } else {
                if(arrivalPlan.getArrivalQty() == arrivalQty) continue;
                arrivalPlan.setArrivalQty(arrivalQty);
                arrivalPlan.setUpdateDate(new java.util.Date());
            }
            arrivalPlanRepository.save(arrivalPlan);
        }
    }


    @Override
    public Page<Map> getPageLcmArrivalPlanMaterial(Date startDate, Date endDate, int page, int limit, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit);
        return arrivalPlanRepository.getPageArrivalPlanMaterial(startDate, endDate,"LCM%", searchMaterialGroup+"%",
                searchMaterial+"%",
                searchSupplier+"%", pageable);
    }

    @Override
    public List<ArrivalPlan> getLcmArrivalPlan(Date startDate, Date endDate, String material, String supplierCode) {
        //LCM1
        List<ArrivalPlan> list1 = arrivalPlanRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual("LCM1",
                material, supplierCode, startDate, endDate);
        //LCM2
        List<ArrivalPlan>  list2 = arrivalPlanRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "LCM2", material, supplierCode, startDate, endDate);
        List<ArrivalPlan> list = new ArrayList<>();
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public Page<Map> getPageCellArrivalPlanMaterial(Date startDate, Date endDate, int page, int limit, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit);
        return arrivalPlanRepository.getPageArrivalPlanMaterial(startDate, endDate,"CELL%", searchMaterialGroup+"%",
                searchMaterial+"%",
                searchSupplier+"%", pageable);
    }

    @Override
    public List<ArrivalPlan> getCellArrivalPlan(Date startDate, Date endDate, String material, String supplierCode) {
        return arrivalPlanRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "CELL", material, supplierCode, startDate, endDate);
    }

    @Override
    public Page<Map> getPageAryArrivalPlanMaterial(Date startDate, Date endDate, int page, int limit, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit);
        return arrivalPlanRepository.getPageArrivalPlanMaterial(startDate, endDate,"ARY%", searchMaterialGroup+"%",
                searchMaterial+"%",
                searchSupplier+"%", pageable);
    }

    @Override
    public List<ArrivalPlan> getAryArrivalPlan(Date startDate, Date endDate, String material, String supplierCode) {
        return arrivalPlanRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "ARY", material, supplierCode, startDate, endDate);
    }

    @Override
    public void saveArrivalPlan(String fab, String material, String supplierCode, Date fabDate, double arrivalQty) {
        ArrivalPlan arrivalPlan= arrivalPlanRepository.findById(new ArrivalPlanKey(fab, material, fabDate, supplierCode)).orElse(null);
        if(arrivalPlan == null) {
            arrivalPlan = new ArrivalPlan();
            arrivalPlan.setFab(fab);
            arrivalPlan.setFabDate(fabDate);
            arrivalPlan.setMaterial(material);
            arrivalPlan.setSupplierCode(supplierCode);
            arrivalPlan.setSupplierSname(supplierService.getSupplier(supplierCode).getSupplierSname());
        }
        arrivalPlan.setArrivalQty(arrivalQty);
        arrivalPlan.setUpdateDate(new java.util.Date());
        arrivalPlanRepository.save(arrivalPlan);
    }

    @Override
    public Page<Map> getPageFillRateMaterial(String fab, Date startDate, Date endDate, int page, int limit, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit);
        return arrivalPlanRepository.getPageFillRateMaterial(startDate, endDate, fab, searchMaterialGroup+"%",
                searchMaterial+"%",
                searchSupplier+"%", pageable);
    }

    @Override
    public List<ArrivalPlan> getArrivalPlan(String fab, Date startDate, Date endDate, String material, String supplierCode) {
        return arrivalPlanRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                fab, material, supplierCode, startDate, endDate);
    }
}
