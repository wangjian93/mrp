package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MonthSettlement;
import com.ivo.mrp2.repository.MonthSettlementRepository;
import com.ivo.mrp2.service.MaterialGroupService;
import com.ivo.mrp2.service.MonthSettlementService;
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
public class MonthSettlementServiceImpl implements MonthSettlementService {

    private MonthSettlementRepository monthSettlementRepository;

    private MaterialGroupService materialGroupService;

    @Autowired
    public MonthSettlementServiceImpl(MonthSettlementRepository monthSettlementRepository, MaterialGroupService materialGroupService) {
        this.monthSettlementRepository = monthSettlementRepository;
        this.materialGroupService = materialGroupService;
    }

    @Override
    public List<MonthSettlement> getMonthSettlement(String plant, String month) {
        return monthSettlementRepository.findByPlantAndMonth(plant, month);
    }

    @Override
    public void save(String plant, String month, String product, String materialGroup, double qty, Date settlementDate) {
        MonthSettlement m = new MonthSettlement();
        m.setPlant(plant);
        m.setMonth(month);
        m.setProduct(product);
        m.setMaterialGroup(materialGroup);
        m.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
        m.setQty(qty);
        m.setSettlementDate(settlementDate);
        monthSettlementRepository.save(m);
    }

    @Override
    public void delete(List<MonthSettlement> list) {
        monthSettlementRepository.deleteAll(list);
    }
}
