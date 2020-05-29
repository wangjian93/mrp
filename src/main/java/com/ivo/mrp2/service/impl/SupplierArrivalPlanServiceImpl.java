package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.repository.SupplierArrivalPlanRepository;
import com.ivo.mrp2.service.SupplierArrivalPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class SupplierArrivalPlanServiceImpl implements SupplierArrivalPlanService {

    private SupplierArrivalPlanRepository arrivalPlanRepository;

    @Autowired
    public SupplierArrivalPlanServiceImpl(SupplierArrivalPlanRepository arrivalPlanRepository) {
        this.arrivalPlanRepository = arrivalPlanRepository;
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
    public Page<Map> pageQuerySupplierArrivalPlan(int page, int limit, String plant, String material,
                                                  String supplier, Date startDate, Date endDate) {
        plant = plant + '%';
        material = material + '%';
        supplier = supplier + '%';
        Pageable pageable = PageRequest.of(page-1, limit);
        return arrivalPlanRepository.pageQuerySupplierArrivalPlan(startDate, endDate, plant, material, supplier, pageable);
    }

    @Override
    public List<Map> getSupplierArrivalQty(Date startDate, Date endDate, String plant, String material, String supplierCode) {
        return arrivalPlanRepository.getSupplierArrivalQty(startDate, endDate, plant, material, supplierCode);
    }
}
