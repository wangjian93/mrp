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

    @Override
    public void delete(List<Demand> list) {
        demandRepository.deleteAll(list);
    }

    @Override
    public boolean isExist(String dpsVer) {
        return demandRepository.findFirstByDpsVer(dpsVer) != null;
    }

    @Override
    public List<Map> sumMaterial(List<String> dpsVers) {
        return demandRepository.sumMaterial(dpsVers);
    }

    @Override
    public List<Map> sumMaterialDemand(List<String> dpsVers, String material) {
        return demandRepository.sumMaterialDemand(dpsVers, material);
    }

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
