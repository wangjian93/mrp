package com.ivo.mrp.service.impl;

import com.ivo.mrp.repository.ArrivalPlanRepository;
import com.ivo.mrp.service.ArrivalPlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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

    @Autowired
    public ArrivalPlanServiceImpl(ArrivalPlanRepository arrivalPlanRepository) {
        this.arrivalPlanRepository = arrivalPlanRepository;
    }

    @Override
    public double getArrivalPlan(String fab, String material, Date fabDate) {
        Double d = arrivalPlanRepository.getArrivalPlan(fab, material, fabDate);
        return d == null ? 0 : d;
    }

    @Override
    public Map<Date, Double> getArrivalPlan(String fab, String material, List<Date> fabDateList) {
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
}
