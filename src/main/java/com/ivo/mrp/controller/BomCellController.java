package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.direct.cell.BomCell2;
import com.ivo.mrp.entity.direct.cell.BomCellMaterial;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.mrp.service.cell.BomCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/bomCell")
public class BomCellController {

    private BomCellService bomCellService;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    @Autowired
    public BomCellController(BomCellService bomCellService, MaterialService materialService,
                             MaterialGroupService materialGroupService) {
        this.bomCellService = bomCellService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
    }

    @GetMapping("/queryBomProduct")
    public Result queryBomProduct(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  @RequestParam(required = false, defaultValue = "") String product) {
        Page p = bomCellService.queryProduct(page-1, limit, product);
        if(p!=null) {
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        } else {
            return ResultUtil.successPage(new ArrayList(), 0);
        }
    }

    @GetMapping("/getBom")
    public Result getBom(String product) {
        List<BomCell2> bomCellList = bomCellService.getBomCell(product);
        List<String> materialList = new ArrayList<>();
        for(BomCell2 bomCell : bomCellList) {
            materialList.add(bomCell.getMaterial());
        }
        List<String> cellMtrlList = bomCellService.getCellMtrl(product);
        List<BomCellMaterial> bomCellMaterialList = bomCellService.getBomCellMaterial(cellMtrlList);
        List<Map> mapList = new ArrayList<>();

        Map<String, Boolean> cellMtrlMap = new HashMap<>();
        for(BomCellMaterial cellMaterial : bomCellMaterialList) {
            String material = cellMaterial.getMTRL_ID();
            Map map = new HashMap();
            map.put("product", product);
            map.put("cellMtrl", cellMaterial.getCELLMTRL());
            map.put("material", material);
            map.put("measureUnit", cellMaterial.getMEASURE_UNIT());
            map.put("usageQty", cellMaterial.getUSAGEQTY());
            map.put("materialName", cellMaterial.getMaterialName());
            map.put("materialGroup", cellMaterial.getMaterialGroup());
            map.put("materialGroupName", cellMaterial.getMaterialGroupName());
            map.put("label", material);
            if(materialList.contains(material)) {
                map.put("LAY_CHECKED", true);
                cellMtrlMap.put(cellMaterial.getCELLMTRL(), true);
            }
            else {
                map.put("LAY_CHECKED", false);
            }
            mapList.add(map);
        }
        for(String cellMtrl : cellMtrlList ) {
            Map map = new HashMap();
            map.put("product", product);
            map.put("label", cellMtrl);
            map.put("material", cellMtrl);
            map.put("cellMtrl", -1);
            map.put("materialName", "");
            map.put("materialGroup", "");
            map.put("materialGroupName", "");
            map.put("LAY_CHECKED", cellMtrlMap.get(cellMtrl) != null);
            mapList.add(map);
        }
        return ResultUtil.success(mapList);
    }

    @PostMapping("/submit")
    public Result submit(String product, String materialData) {
        ObjectMapper mapper = new ObjectMapper();
        List<BomCell2> oldBomCellList = bomCellService.getBomCell(product);
        List<BomCell2> bomCellList = new ArrayList<>();
        try {
            List<Map> mapList;
            List<String> materialList = new ArrayList<>();
            mapList = mapper.readValue(materialData, new TypeReference<List<Map>>() {});
            if(mapList != null && mapList.size()>0) {
                for(Map map : mapList) {
                    String material = (String) map.get("material");
                    if(materialList.contains(material)) continue;
                    materialList.add(material);
                    double usageQty = (Double) map.get("usageQty");
                    String measureUnit = (String) map.get("measureUnit");
                    String materialName = materialService.getMaterialName(material);
                    String materialGroup = materialService.getMaterialGroup(material);
                    String materialGroupName = materialGroupService.getMaterialGroupName(materialGroup);

                    BomCell2 bomCell = new BomCell2();
                    bomCell.setProduct(product);
                    bomCell.setMaterial(material);
                    bomCell.setUsageQty(usageQty);
                    bomCell.setMeasureUnit(measureUnit);
                    bomCell.setMaterialName(materialName);
                    bomCell.setMaterialGroup(materialGroup);
                    bomCell.setMaterialGroupName(materialGroupName);
                    bomCell.setId(product+"_"+material);
                    bomCellList.add(bomCell);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bomCellService.deleteBomCell(oldBomCellList);
        bomCellService.saveBomCell(bomCellList);
        return ResultUtil.success("提供成功");
    }
}
