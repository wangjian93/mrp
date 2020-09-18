package com.ivo.mrp.service.impl;

import com.ivo.mrp.repository.AllocationRepository;
import com.ivo.mrp.service.AllocationService;
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
public class AllocationServiceImpl implements AllocationService {

    private AllocationRepository allocationRepository;

    @Autowired
    public AllocationServiceImpl(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    @Override
    public double getAllocation(String fab, String material, Date fabDate) {
        Double d = allocationRepository.getAllocation(fab, material, fabDate);
        return d==null ? 0 : d;
    }

    @Override
    public Map<Date, Double> getAllocation(String fab, String material, List<Date> fabDateList) {
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
}
