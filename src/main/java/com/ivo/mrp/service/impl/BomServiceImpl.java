package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.direct.ary.BomAry;
import com.ivo.mrp.entity.direct.cell.BomCell;
import com.ivo.mrp.entity.direct.cell.BomCellMtrl;
import com.ivo.mrp.entity.direct.lcm.BomLcm;
import com.ivo.mrp.key.BomCellKey;
import com.ivo.mrp.key.BomCellMtrlKey;
import com.ivo.mrp.repository.BomAryRepository;
import com.ivo.mrp.repository.BomCellMtrlRepository;
import com.ivo.mrp.repository.BomCellRepository;
import com.ivo.mrp.repository.BomLcmRepository;
import com.ivo.mrp.service.BomService;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class BomServiceImpl implements BomService {

    private RestService restService;

    private BomCellRepository bomCellRepository;

    private BomLcmRepository bomLcmRepository;

    private BomAryRepository bomAryRepository;

    private BomCellMtrlRepository bomCellMtrlRepository;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public BomServiceImpl(RestService restService, BomCellRepository bomCellRepository,
                          BomLcmRepository bomLcmRepository, BomAryRepository bomAryRepository,
                          BomCellMtrlRepository bomCellMtrlRepository,
                          MaterialService materialService, MaterialGroupService materialGroupService) {
        this.restService = restService;
        this.bomCellRepository = bomCellRepository;
        this.bomLcmRepository = bomLcmRepository;
        this.bomAryRepository = bomAryRepository;
        this.bomCellMtrlRepository = bomCellMtrlRepository;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    @Override
    public void syncBomLcm() {
        log.info("同步LCM的BOM >> START");
        //清空表
        bomLcmRepository.deleteAll();
        syncBomLcm1();
        syncBomLcm2();
        log.info("同步LCM的BOM >> END");
    }

    private void syncBomLcm1() {
        log.info("LCM1 BOM...");
        List<Map> mapList = restService.getBomLcm1();
        if(mapList == null || mapList.size() == 0) return;
        for(Map map : mapList) {
            BomLcm bomLcm = new BomLcm();
            bomLcm.setFab("LCM1");
            bomLcm.setProduct((String) map.get("product"));
            bomLcm.setMaterial((String) map.get("material"));
            bomLcm.setMeasureUnit((String) map.get("measureUnit"));
            bomLcm.setMaterialName((String) map.get("materialName"));
            bomLcm.setMaterialGroup((String) map.get("materialGroup"));
            bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
            bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());
            bomLcmRepository.save(bomLcm);
        }

    }

    private void syncBomLcm2() {
        log.info("LCM2 BOM...");
        List<Map> mapList = restService.getBomLcm2();
        if(mapList == null || mapList.size() == 0) return;
        for(Map map : mapList) {
            BomLcm bomLcm = new BomLcm();
            bomLcm.setFab("LCM2");
            bomLcm.setProduct((String) map.get("product"));
            bomLcm.setMaterial((String) map.get("material"));
            bomLcm.setMeasureUnit((String) map.get("measureUnit"));
            bomLcm.setMaterialName((String) map.get("materialName"));
            bomLcm.setMaterialGroup((String) map.get("materialGroup"));
            bomLcm.setMaterialGroupName((String) map.get("materialGroupName"));
            bomLcm.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());
            bomLcmRepository.save(bomLcm);
        }
    }

    @Override
    public void syncBomAry() {
        log.info("同步ARY的BOM >> START");
        //清空表
        bomAryRepository.deleteAll();
        List<Map> mapList = restService.getBomAry();
        if(mapList == null || mapList.size() == 0) return;
        for(Map map : mapList) {
            BomAry bomAry = new BomAry();
            bomAry.setFab("ARY");
            bomAry.setProduct((String) map.get("product"));
            bomAry.setMaterial((String) map.get("material"));
            bomAry.setMeasureUnit((String) map.get("measureUnit"));
            bomAry.setMaterialName((String) map.get("materialName"));
            bomAry.setMaterialGroup((String) map.get("materialGroup"));
            bomAry.setMaterialGroupName((String) map.get("materialGroupName"));
            bomAry.setUsageQty(((BigDecimal) map.get("usageQty")).doubleValue());
            bomAryRepository.save(bomAry);
        }
        log.info("同步ARY的BOM >> END");
    }

    @Override
    public void syncBomCell() {
        log.info("同步CELL的BOM >> START");
        List<Map> cellMtrlList = restService.getBomCell();
        if(cellMtrlList == null || cellMtrlList.size() == 0) return;
        int l = cellMtrlList.size();
        for(Map cellMtrlMap : cellMtrlList) {
            log.info("成品料号剩余"+l--);
            //1.同步MPS的CellInPut数据
            String product = (String) cellMtrlMap.get("CellInPut");
            String cellMtrl = (String) cellMtrlMap.get("Material_FK");
            String project = (String) cellMtrlMap.get("project");
            if(StringUtils.isEmpty(product) || StringUtils.isEmpty(cellMtrl)) continue;
            BomCell bomCell = getBomCell(product, cellMtrl);
            if(bomCell == null) {
                bomCell = new BomCell();
                bomCell.setFab("CELL");
                bomCell.setProject(project);
                bomCell.setProduct(product);
                bomCell.setCellMtrl(cellMtrl);
                bomCell.setUseFlag(true);
                bomCellRepository.save(bomCell);
            }
            //2.同步成品料号的材料
            List<Map> materialMapList = restService.getCellMtrl(cellMtrl);
            if(materialMapList == null || materialMapList.size()==0) continue;
            for(Map materialMap : materialMapList) {
                String material = (String) materialMap.get("MTRL_ID");
                BomCellMtrl bomCellMtrl = getBomCellMtrl(product, cellMtrl, material);
                if(bomCellMtrl == null) {
                    bomCellMtrl = new BomCellMtrl();
                    bomCellMtrl.setFab("CELL");
                    bomCellMtrl.setProject(project);
                    bomCellMtrl.setProduct(product);
                    bomCellMtrl.setCellMtrl(cellMtrl);
                    bomCellMtrl.setUseFlag(true);
                    bomCellMtrl.setMaterial(material);
                    String materialGroup = materialService.getMaterialGroup(material);
                    bomCellMtrl.setMaterialName(materialService.getMaterialName(material));
                    bomCellMtrl.setMaterialGroup(materialGroup);
                    bomCellMtrl.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
                    bomCellMtrl.setMeasureUnit((String) materialMap.get("MEASURE_UNIT"));
                    bomCellMtrl.setUsageQty( ((BigDecimal) materialMap.get("USAGEQTY")).doubleValue());
                    bomCellMtrlRepository.save(bomCellMtrl);
                }
            }
        }
        log.info("同步CELL的BOM >> END");
    }

    private BomCell getBomCell(String product, String cellMtrl) {
        BomCellKey bomCellKey = new BomCellKey("CELL", product, cellMtrl);
        return bomCellRepository.findById(bomCellKey).orElse(null);
    }

    private BomCellMtrl getBomCellMtrl(String product, String cellMtrl, String material) {
        BomCellMtrlKey bomCellMtrlKey = new BomCellMtrlKey("CELL", product, cellMtrl, material);
        return bomCellMtrlRepository.findById(bomCellMtrlKey).orElse(null);
    }

    @Override
    public List<BomLcm> getLcmBom(String product, String fab) {
        return bomLcmRepository.findByFabAndProduct(fab, product);
    }

    @Override
    public List<BomAry> getAryBom(String product) {
        return bomAryRepository.findByProduct(product);
    }

    @Override
    public List<Map> getAryOcBom(String product) {
        return bomAryRepository.getAryOcBom(product);
    }

    @Override
    public List<BomCellMtrl> getCellBom(String product) {
        return bomCellMtrlRepository.findByProductAndUseFlag(product, true);
    }

    @Override
    public List<Map> queryCellBom(String product) {
        return bomCellRepository.findByProduct(product);
    }

    @Override
    public Page<Map> queryProduct(int page, int limit, String fab, String searchProduct) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product");
        if(StringUtils.equalsAnyIgnoreCase(fab, "LCM1", "LCM2")) {
            return bomLcmRepository.queryProduct(fab, searchProduct+"%", pageable);
        } else if(StringUtils.equalsIgnoreCase(fab, "CELL")) {
            return bomCellRepository.queryProduct(fab, searchProduct+"%", pageable);
        } else if(StringUtils.equalsIgnoreCase(fab, "ARY")) {
            return bomAryRepository.queryProduct(fab, searchProduct+"%", pageable);
        }
        return null;
    }
}
