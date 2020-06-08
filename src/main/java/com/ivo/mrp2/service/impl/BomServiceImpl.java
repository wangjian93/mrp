package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.repository.BomRepository;
import com.ivo.mrp2.service.BomService;
import com.ivo.rest.eif.service.EifBomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class BomServiceImpl implements BomService {

    private BomRepository bomRepository;

    private EifBomService eifBomService;

    @Autowired
    public BomServiceImpl(BomRepository bomRepository, EifBomService eifBomService) {
        this.bomRepository = bomRepository;
        this.eifBomService = eifBomService;
    }

    @Override
    public void syncBom() {
        log.info("START>> 从Auto PR同步LCM1/LCM2/CELL的BOM");
        bomRepository.deleteAll();
        log.info("LCM1...");
        List<Bom> lcm1BomList = eifBomService.getBomLcm1();
        bomRepository.saveAll(lcm1BomList);
        log.info("LCM2...");
        List<Bom> lcm2BomList = eifBomService.getBomLcm2();
        bomRepository.saveAll(lcm2BomList);
        log.info("CELL...");
        List<Bom> cellBomList = eifBomService.getBomCell();
        bomRepository.saveAll(cellBomList);
        log.info("END>> BOM同步完成");
    }

    @Override
    public String getMaterialGroup(String material) {
        return bomRepository.getMaterialGroup(material);
    }

    @Override
    public String getMaterialName(String material) {
        return bomRepository.getMaterialName(material);
    }

    @Override
    public Page<Bom> pageQueryBom(int page, int limit, String product, String material, String plant) {
        Pageable pageable = PageRequest.of(page, limit);
        if(product == null) {
            product = "";
        }
        if(material == null)  {
            product = "";
        }
        if(plant == null) {
            plant = "";
        }
        product += "%";
        material += "%";
        plant += "%";
        return bomRepository.findByProductLikeAndMaterialLikeAndPlantLike(product, material, plant, pageable);
    }

    @Override
    public List<Bom> getBomByProductAndPlant(String product, String plant) {
        return bomRepository.findByPlantAndProduct(plant, product);
    }

    @Override
    public List<Map> getMaterialNameAndGroup(List<String> materialList) {
        return bomRepository.getMaterialNameAndGroup(materialList);
    }
}
