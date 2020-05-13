package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Demand;
import com.ivo.mrp2.entity.MaterialDailyDemandTemp;
import com.ivo.mrp2.repository.DemandRepository;
import com.ivo.mrp2.repository.MaterialDailyDemandTempRepository;
import com.ivo.mrp2.service.DemandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DemandServiceImpl implements DemandService {

    private DemandRepository demandRepository;

    private MaterialDailyDemandTempRepository tempRepository;

    @Autowired
    public DemandServiceImpl(DemandRepository demandRepository,
                             MaterialDailyDemandTempRepository tempRepository) {
        this.demandRepository = demandRepository;
        this.tempRepository = tempRepository;
    }

    @Override
    public void batchSave(List<Demand> list) {
        demandRepository.saveAll(list);
    }

    @Override
    public void batchSaveTemp(List<MaterialDailyDemandTemp> list) {
        tempRepository.saveAll(list);
    }

    @Override
    public List<Map> summaryMaterialDemand(String mrpVer) {
        return tempRepository.summaryMaterialDemandTemp(mrpVer);
    }

    @Override
    public List<Map> summaryMaterial(String dpsVer) {
        return demandRepository.summaryMaterial(dpsVer);
    }

    @Override
    public Demand getMaterialDailyDemandLoss(String mrpVer, String material, Date fabDate) {
        return demandRepository.findByDpsVerAndMaterialAndFabDate(mrpVer, material, fabDate);
    }

    @Override
    public List<Demand> getMaterialDailyDemandLoss(String mrpVer) {
        return demandRepository.findByDpsVer(mrpVer);
    }

    @Override
    public List<Demand> getMaterialDailyDemandLoss(String mrpVer, String material) {
        return demandRepository.findByDpsVerAndMaterial(mrpVer, material);
    }

    @Override
    public List<MaterialDailyDemandTemp> getMaterialDailyDemandTemp(String mrpVer) {
        return tempRepository.findByMrpVer(mrpVer);
    }

    @Override
    public List<MaterialDailyDemandTemp> getMaterialDailyDemandTemp(String mrpVer, String material) {
        return tempRepository.findByMrpVerAndMaterial(mrpVer, material);
    }

    @Override
    public List<Demand> getDemand(String dpsVer) {
        return demandRepository.findByDpsVer(dpsVer);
    }

    @Override
    public List<Demand> getDemand(String dpsVer, Date fabDate, String material) {
        return demandRepository.findByDpsVerAndFabDateAndMaterial(dpsVer, fabDate, material);
    }

    @Override
    public List<Demand> getDemand(String dpsVer, String product) {
        return demandRepository.findByDpsVerAndProduct(dpsVer, product);
    }
}
