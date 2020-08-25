package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MrpAllocation;
import com.ivo.mrp2.key.MrpAllocationPrimaryKey;
import com.ivo.mrp2.repository.MrpAllocationRepository;
import com.ivo.mrp2.service.MrpAllocationService;
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
public class MrpAllocationServiceImpl implements MrpAllocationService {

    private MrpAllocationRepository mrpAllocationRepository;

    @Autowired
    public MrpAllocationServiceImpl(MrpAllocationRepository mrpAllocationRepository) {
        this.mrpAllocationRepository = mrpAllocationRepository;
    }

    @Override
    public List<MrpAllocation> list() {
        return mrpAllocationRepository.findAll();
    }

    @Override
    public List<MrpAllocation> getMrpAllocation(String plant, String material, Date fabDate) {
        return mrpAllocationRepository.findByPlantAndMaterialAndAdate(plant, material, fabDate);
    }

    @Override
    public MrpAllocation getMrpAllocation(String plant, String material, Date fabDate, String supplier) {
        MrpAllocationPrimaryKey mrpAllocationPrimaryKey = new MrpAllocationPrimaryKey(plant, fabDate, material, supplier);
        return mrpAllocationRepository.findById(mrpAllocationPrimaryKey).orElse(null);
    }

    @Override
    public double getAllocationQry(String plant, String material, Date fabDate) {
        Double d = mrpAllocationRepository.getAllocationQty(plant, material, fabDate);
        return d==null ? 0 : d;
    }

    @Override
    public void saveMrpAllocation(List<MrpAllocation> mrpAllocationList) {
        mrpAllocationRepository.saveAll(mrpAllocationList);
    }
}
