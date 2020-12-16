package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.lcmPackaging.DemandPackageLcm;
import com.ivo.mrp.repository.lcmPackage.DemandPackageLcmRepository;
import com.ivo.mrp.service.DemandPackageLcmService;
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
@Service
@Slf4j
public class DemandPackageLcmServiceImpl implements DemandPackageLcmService {

    private DemandPackageLcmRepository demandPackageLcmRepository;

    @Autowired
    public DemandPackageLcmServiceImpl(DemandPackageLcmRepository demandPackageLcmRepository) {
        this.demandPackageLcmRepository = demandPackageLcmRepository;
    }

    @Override
    public void saveDemandPackageLcm(List<DemandPackageLcm> list) {
        demandPackageLcmRepository.saveAll(list);
    }

    @Override
    public List<String> getDemandAloneMaterial(String ver) {
        return demandPackageLcmRepository.getDemandAloneMaterial(ver);
    }

    @Override
    public List<Map> getDemandProductMaterial(String ver) {
        return demandPackageLcmRepository.getDemandProductMaterial(ver);
    }

    @Override
    public Map<Date, Double> getDemandQtyAlone(String ver, String material) {
        List<Map> mapList = demandPackageLcmRepository.getDemandQtyAlone(ver, material);
        Map<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double demandQty = (Double) map.get("demandQty");
            m.put(fabDae, demandQty);
        }
        return m;
    }

    @Override
    public Map<Date, Double> getDemandQtyProduct(String ver, String product, String material) {
        List<Map> mapList = demandPackageLcmRepository.getDemandQtyProduct(ver, product, material);
        Map<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double demandQty = (Double) map.get("demandQty");
            m.put(fabDae, demandQty);
        }
        return m;
    }

    @Override
    public List<String> getDemandAloneMaterialProduct(String ver, String material) {
        return demandPackageLcmRepository.getDemandAloneMaterialProduct(ver, material);
    }

    @Override
    public List<Map> getDemandQtyAloneDetail(String ver, String material) {
        return demandPackageLcmRepository.getDemandQtyAloneDetail(ver, material);
    }
}
