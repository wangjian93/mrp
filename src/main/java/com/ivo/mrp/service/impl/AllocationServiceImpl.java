package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.Allocation;
import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.key.AllocationKey;
import com.ivo.mrp.key.ArrivalPlanKey;
import com.ivo.mrp.repository.AllocationRepository;
import com.ivo.mrp.service.AllocationService;
import com.ivo.mrp.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AllocationServiceImpl implements AllocationService {

    private AllocationRepository allocationRepository;
    private SupplierService supplierService;

    @Autowired
    public AllocationServiceImpl(AllocationRepository allocationRepository, SupplierService supplierService) {
        this.allocationRepository = allocationRepository;
        this.supplierService = supplierService;
    }

    @Override
    public double getAllocationQty(String fab, String material, Date fabDate) {
        Double d = allocationRepository.getAllocation(fab, material, fabDate);
        return d==null ? 0 : d;
    }

    @Override
    public Map<Date, Double> getAllocationQty(String fab, String material, List<Date> fabDateList) {
        List<Map> mapList = allocationRepository.getAllocation(fab, material, fabDateList);
        HashMap<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double allocationQty = (Double) map.get("allocationQty");
            m.put(fabDae, allocationQty);
        }
        return m;
    }

    @Override
    public double getAllocationPackage(String fab, String product, String type, Double linkQty, String mode, String material, Date fabDate) {
        return 0;
    }

    @Override
    public List<Allocation> getAllocation(String fab, String material, Date fabDate) {
        return allocationRepository.findByFabAndMaterialAndFabDate(fab, material, fabDate);
    }

    @Override
    public void batchSaveAllocation(List<Map> mapList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Map map : mapList) {
            String material = (String) map.get("material");
            String fabDate_ = (String) map.get("fabDate");
            String fab = (String) map.get("fab");
            String supplierCode = (String) map.get("supplierCode");
            double allocationQty = Double.parseDouble( String.valueOf(map.get("allocationQty")) );
            Date fabDate = null;
            try {
                fabDate = new Date(sdf.parse(fabDate_).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Allocation allocation = allocationRepository.findById(new AllocationKey(fab, material, fabDate, supplierCode)).orElse(null);
            if(allocation == null) {
                if(allocationQty == 0) continue;
                allocation = new Allocation();
                allocation.setFab(fab);
                allocation.setMaterial(material);
                allocation.setFabDate(fabDate);
                allocation.setSupplierCode(supplierCode);
                allocation.setAllocationQty(allocationQty);

                Supplier supplier = supplierService.getSupplier(supplierCode);
                if(supplier != null) {
                    allocation.setSupplierSname(supplier.getSupplierSname());
                }
            } else {
                if(allocation.getAllocationQty() == allocationQty) continue;
                allocation.setAllocationQty(allocationQty);
                allocation.setUpdateDate(new java.util.Date());
            }
            allocationRepository.save(allocation);
        }
    }


    @Override
    public List<Allocation> getLcmAllocation(Date startDate, Date endDate, String material, String supplierCode) {
        List list1 = allocationRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "LCM1", material, supplierCode, startDate, endDate);
        List list2 = allocationRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "LCM2", material, supplierCode, startDate, endDate);
        List list = new ArrayList();
        list.addAll(list1);
        list.addAll(list2);
        return list;
    }

    @Override
    public List<Allocation> getAryAllocation(Date startDate, Date endDate, String material, String supplierCode) {
        return allocationRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "ARY", material, supplierCode, startDate, endDate);
    }

    @Override
    public List<Allocation> getCellAllocation(Date startDate, Date endDate, String material, String supplierCode) {
        return allocationRepository.findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                "CELL", material, supplierCode, startDate, endDate);
    }
}
