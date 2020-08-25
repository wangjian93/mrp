package com.ivo.mrp2.service.impl;

import com.ivo.common.BatchService;
import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.entity.Material;
import com.ivo.mrp2.key.BomPrimaryKey;
import com.ivo.mrp2.repository.BomRepository;
import com.ivo.mrp2.service.BomService;
import com.ivo.mrp2.service.MaterialGroupService;
import com.ivo.mrp2.service.MaterialService;
import com.ivo.rest.eif.service.EifBomService;
import com.ivo.rest.fcst.service.FcstService;
import com.ivo.rest.oracle.service.CellBomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    private BatchService batchService;

    private FcstService fcstService;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    private CellBomService cellBomService;

    @Autowired
    public BomServiceImpl(BomRepository bomRepository, EifBomService eifBomService, BatchService batchService,
                          FcstService fcstService,
                          MaterialService materialService,
                          MaterialGroupService materialGroupService,
                          CellBomService cellBomService) {
        this.bomRepository = bomRepository;
        this.eifBomService = eifBomService;
        this.batchService = batchService;
        this.fcstService = fcstService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.cellBomService = cellBomService;
    }

    @Override
    public void syncBom() {
        log.info("从Auto PR同步LCM1/LCM2/CELL的BOM>> START");
//        log.info("清空表MRP2_BOM");
//        bomRepository.truncate();
//        List<Bom> lcm1BomList = eifBomService.getBomLcm1();
//        List<Bom> lcm2BomList = eifBomService.getBomLcm2();
//        List<Bom> aryBomList = eifBomService.getBomAry();
//        List<Bom> bomList = new ArrayList<>();
//        bomList.addAll(lcm1BomList);
//        bomList.addAll(lcm2BomList);
//        bomList.addAll(aryBomList);
//        save(bomList);
        //CELL
        List<Bom> cellBomList = new ArrayList<>();
        List<Map> cellMaterials = fcstService.getCellMaterial();
        int i= cellMaterials.size();
        for(Map map : cellMaterials) {
            System.out.println("剩余" + i--);
            String cellInPut = (String) map.get( ("cellInPut") );
            String material = (String) map.get( ("material") );
            Bom bom = new Bom();
            bom.setPlant("CELL");
            bom.setProduct(cellInPut);
            bom.setMaterial(material);
            bom.setMaterial_("");
            bom.setEffectFlag(true);
            bom.setCreateDate(new Date());
            bom.setMemo("MPS同步成品料号");
            Material m = materialService.getMaterial(material);
            if(m != null) {
                bom.setMaterialName(m.getMaterialName());
                bom.setMaterialGroup(m.getMaterialGroup());
                bom.setMaterialGroupName(materialGroupService.getMaterialGroupName(m.getMaterialGroup()));
            }
            cellBomList.add(bom);

            //成品料号展开
            List<String> materialList = new ArrayList<>();
            materialList.add(material);
            List<Map> bomMapList = cellBomService.getCellBom(materialList);
            for(Map bomMap : bomMapList) {
                String material_ = (String) bomMap.get("MATERIAL");
                if(material_ == null) continue;
                Bom bom1 = new Bom();
                bom1.setPlant("CELL");
                bom1.setProduct(cellInPut);
                bom1.setMaterial(material);
                bom1.setMaterial_(material_);
                bom1.setEffectFlag(true);
                bom1.setCreateDate(new Date());
                bom1.setMemo("MPS同步成品料号");
                bom1.setMeasureUnit((String) bomMap.get( ("measureUnit").toUpperCase() ));
                bom1.setUsageQty( ((BigDecimal) bomMap.get( ("usageQty").toUpperCase() )).doubleValue());
                Material m_ = materialService.getMaterial(material_);
                if(m != null) {
                    bom1.setMaterialName(m_.getMaterialName());
                    bom1.setMaterialGroup(m_.getMaterialGroup());
                    bom1.setMaterialGroupName(materialGroupService.getMaterialGroupName(m_.getMaterialGroup()));
                }

                BomPrimaryKey bomPrimaryKey = new BomPrimaryKey(bom1.getPlant(), bom1.getProduct(), bom1.getMaterial(), bom1.getMaterial_());
                Bom bom2 = bomRepository.findById(bomPrimaryKey).orElse(null);
                if(bom2 != null) {
                    continue;
                }

                cellBomList.add(bom1);
            }
        }
        save(cellBomList);
        log.info("从Auto PR同步LCM1/LCM2/CELL的BOM>> END");
    }

    @Override
    public List<Bom> getAllBom() {
        return bomRepository.findAll();
    }

    @Override
    public List<Bom> getProductBom(String plant, String product) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "material");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "material_");
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        Sort sort = Sort.by(orderList);
        return bomRepository.findByPlantAndProduct(plant, product, sort);
    }

    @Override
    public List<Bom> getMaterialGroupBom(String plant, String product, String materialGroup) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "material");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "material_");
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        Sort sort = Sort.by(orderList);
        return bomRepository.findByPlantAndProductAndMaterialGroup(plant, product, materialGroup, sort);
    }

    @Override
    public Bom getBom(String plant, String product, String material, String material_) {
        return bomRepository.getOne(new BomPrimaryKey(plant, product, material, material_));
    }

    @Override
    public void save(List<Bom> bomList) {
        log.info("保存BOM>> START");
        bomRepository.saveAll(bomList);
        log.info("保存BOM>> END");
    }

    @Override
    public Page<String> searchProduct(int page, int limit, String plant, String product) {
        Pageable pageable = PageRequest.of(page, limit);
        return bomRepository.searchProduct(plant, product+"%", pageable);
    }

    @Override
    public List<String> getMaterialGroup(String plant, String product) {
        return bomRepository.getMaterialGroup(plant, product);
    }

    @Override
    public List<String> getMaterial(String plant, String product, String materialGroup) {
        return bomRepository.getMaterial(plant, product, materialGroup);
    }
}
