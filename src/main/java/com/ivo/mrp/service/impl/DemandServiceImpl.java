package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.direct.ary.DemandAry;
import com.ivo.mrp.entity.direct.ary.DemandAryOc;
import com.ivo.mrp.entity.direct.cell.DemandCell;
import com.ivo.mrp.entity.direct.lcm.DemandLcm;
import com.ivo.mrp.repository.DemandAryOcRepository;
import com.ivo.mrp.repository.DemandAryRepository;
import com.ivo.mrp.repository.DemandCellRepository;
import com.ivo.mrp.repository.DemandLcmRepository;
import com.ivo.mrp.service.DemandService;
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
public class DemandServiceImpl implements DemandService {

    private DemandAryRepository demandAryRepository;

    private DemandAryOcRepository demandAryOcRepository;

    private DemandCellRepository demandCellRepository;

    private DemandLcmRepository demandLcmRepository;

    @Autowired
    public DemandServiceImpl(DemandAryRepository demandAryRepository, DemandAryOcRepository demandAryOcRepository,
                             DemandCellRepository demandCellRepository, DemandLcmRepository demandLcmRepository) {
        this.demandAryRepository = demandAryRepository;
        this.demandAryOcRepository = demandAryOcRepository;
        this.demandCellRepository = demandCellRepository;
        this.demandLcmRepository = demandLcmRepository;
    }

    @Override
    public void saveDemandLcm(List<DemandLcm> demandLcmList) {
        demandLcmRepository.saveAll(demandLcmList);
    }

    @Override
    public void saveDemandAry(List<DemandAry> demandAryList) {
        demandAryRepository.saveAll(demandAryList);
    }

    @Override
    public void saveDemandAryOc(List<DemandAryOc> demandAryOcList) {
        demandAryOcRepository.saveAll(demandAryOcList);
    }

    @Override
    public void saveDemandCell(List<DemandCell> demandCellList) {
        demandCellRepository.saveAll(demandCellList);
    }

    @Override
    public List<String> getDemandMaterialLcm(String ver) {
        return demandLcmRepository.getMaterial(ver);
    }

    @Override
    public List<String> getDemandMaterialAry(String ver) {
        return demandAryRepository.getMaterial(ver);
    }

    @Override
    public List<String> getDemandMaterialCell(String ver) {
        return demandCellRepository.getMaterial(ver);
    }

    @Override
    public Map<Date, Double> getDemandQtyLcm(String ver, String material) {
        List<Map> mapList = demandLcmRepository.getDemandQtyLcm(ver, material);
        Map<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double demandQty = (Double) map.get("demandQty");
            m.put(fabDae, demandQty);
        }
        return m;
    }

    @Override
    public Map<Date, Double> getDemandQtyAry(String ver, String material) {
        List<Map> mapList = demandAryRepository.getDemandQtyAry(ver, material);
        Map<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double demandQty = (Double) map.get("demandQty");
            m.put(fabDae, demandQty);
        }
        return m;
    }

    @Override
    public Map<Date, Double> getDemandQtyCell(String ver, String material) {
        List<Map> mapList = demandCellRepository.getDemandQtyCell(ver, material);
        Map<Date, Double> m = new HashMap<>();
        for(Map map : mapList) {
            Date fabDae = (Date) map.get("fabDate");
            Double demandQty = (Double) map.get("demandQty");
            m.put(fabDae, demandQty);
        }
        return m;
    }

    @Override
    public double getDemandQtyLcm(String ver, String material, Date fabDate) {
        Double d = demandLcmRepository.getDemandQtyLcm(ver, material, fabDate);
        return d == null ? 0 : d;
    }

    @Override
    public double getDemandQtyAry(String ver, String material, Date fabDate) {
        Double d = demandAryRepository.getDemandQtyAry(ver, material, fabDate);
        return d == null ? 0 : d;
    }

    @Override
    public double getDemandQtyCell(String ver, String material, Date fabDate) {
        Double d = demandCellRepository.getDemandQtyCell(ver, material, fabDate);
        return d == null ? 0 : d;
    }
}
