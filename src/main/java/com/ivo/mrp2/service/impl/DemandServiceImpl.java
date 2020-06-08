package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Demand;
import com.ivo.mrp2.repository.DemandRepository;
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

    @Autowired
    public DemandServiceImpl(DemandRepository demandRepository) {
        this.demandRepository = demandRepository;
    }

    @Override
    public void batchSave(List<Demand> list) {
        demandRepository.saveAll(list);
    }


//    @Override
//    public List<Map> summaryMaterial(String dpsVer) {
//        return demandRepository.summaryMaterial(dpsVer);
//    }
//
//    @Override
//    public Demand getMaterialDailyDemandLoss(String mrpVer, String material, Date fabDate) {
//        return demandRepository.findByDpsVerAndMaterialAndFabDate(mrpVer, material, fabDate);
//    }
//
//    @Override
//    public List<Demand> getMaterialDailyDemandLoss(String mrpVer) {
//        return demandRepository.findByDpsVer(mrpVer);
//    }
//
//    @Override
//    public List<Demand> getMaterialDailyDemandLoss(String mrpVer, String material) {
//        return demandRepository.findByDpsVerAndMaterial(mrpVer, material);
//    }
//
//    @Override
//    public List<Demand> getDemand(String dpsVer) {
//        return demandRepository.findByDpsVer(dpsVer);
//    }

    @Override
    public List<Demand> getDemand(String dpsVer, Date fabDate, String material) {
        return demandRepository.findByDpsVerAndFabDateAndMaterial(dpsVer, fabDate, material);
    }

//    @Override
//    public List<Demand> getDemand(String dpsVer, String product) {
//        return demandRepository.findByDpsVerAndProduct(dpsVer, product);
//    }


    @Override
    public List<String> getMaterial(String dpsVer) {
        return demandRepository.getMaterial(dpsVer);
    }

    @Override
    public List<Demand> getDemandByMaterial(String dpsVer, String material) {
        return demandRepository.findByDpsVerAndMaterial(dpsVer, material);
    }


    @Override
    public List<Demand> getDemand(String dpsVer) {
        return demandRepository.findByDpsVer(dpsVer);
    }
}
