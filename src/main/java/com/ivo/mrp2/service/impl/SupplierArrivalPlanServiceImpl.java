package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.repository.SupplierArrivalPlanRepository;
import com.ivo.mrp2.service.SupplierArrivalPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

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
    public Double getMaterialArrivalPlanQty(String material, Date date) {
        Double d = arrivalPlanRepository.getMaterialArrivalPlanQty(material, date);
        if(d == null) return 0D;
        return d;
    }

    @Override
    public List<SupplierArrivalPlan> getSupplierArrivalPlan(Date fromDate, Date toDate, String material) {
        return arrivalPlanRepository.getSupplierArrivalPlan(fromDate, toDate, material);
    }

    @Override
    public List<SupplierArrivalPlan> getSupplierArrivalPlan(Date fromDate, Date toDate) {
        return arrivalPlanRepository.getSupplierArrivalPlan(fromDate, toDate);
    }
}
