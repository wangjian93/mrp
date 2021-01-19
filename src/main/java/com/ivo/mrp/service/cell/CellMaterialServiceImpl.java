package com.ivo.mrp.service.cell;

import com.ivo.common.BatchService;
import com.ivo.mrp.entity.MaterialGroup;
import com.ivo.mrp.entity.direct.cell.CellMaterial;
import com.ivo.mrp.repository.cell.CellMaterialRepository;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class CellMaterialServiceImpl implements CellMaterialService {

    private CellMaterialRepository cellMaterialRepository;

    private RestService restService;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    private BatchService batchService;

    @Autowired
    public CellMaterialServiceImpl(CellMaterialRepository cellMaterialRepository, RestService restService,
                                   MaterialService materialService, MaterialGroupService materialGroupService,
                                   BatchService batchService) {
        this.cellMaterialRepository = cellMaterialRepository;
        this.restService = restService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.batchService = batchService;
    }

    @Override
    public void syncCellMaterial() {
        log.info("同步CELL料号的材料 >> START");
        //清空表
        log.info("清空表CellMaterial..");
        cellMaterialRepository.truncateTable();
        List<Map> mapList = restService.getCellMtrl();
        if(mapList==null || mapList.size()==0) return;
        List<CellMaterial> cellMaterialList = new ArrayList<>();
        for(Map map : mapList) {
            CellMaterial cellMaterial = new CellMaterial();
            cellMaterial.setPLANT((String) map.get("PLANT"));
            cellMaterial.setPRODUCT((String) map.get("PRODUCT"));
            cellMaterial.setCELLMTRL((String) map.get("CELLMTRL"));
            cellMaterial.setMTRL_ID((String) map.get("MTRL_ID"));
            cellMaterial.setMEASURE_UNIT((String) map.get("MEASURE_UNIT"));
            cellMaterial.setUSAGEQTY( ((BigDecimal) map.get("USAGEQTY")).doubleValue() );
            cellMaterial.setSUBFLAG( ((BigDecimal) map.get("SUBFLAG")).intValue() );
            cellMaterial.setMEMO((String) map.get("MEMO"));

            String material = cellMaterial.getMTRL_ID();
            cellMaterial.setMaterialName(materialService.getMaterialName(material));
            String materialGroup = materialService.getMaterialGroup(material);
            cellMaterial.setMaterialGroup(materialGroup);
            cellMaterial.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));

            if(StringUtils.equalsAny(cellMaterial.getMaterialGroup(), "104","103", "115", "116", "101",
                    "922", "921", "917", "918")
                    || StringUtils.startsWith(cellMaterial.getMTRL_ID(), "57")) {
                cellMaterial.setType(CellMaterial.TYPE_P);
            } else {
                cellMaterial.setType(CellMaterial.TYPE_M);
            }

            cellMaterialList.add(cellMaterial);
        }
        log.info("保存数据CellMaterial..");
        batchService.batchInsert(cellMaterialList);
        log.info("同步CELL料号的材料 >> END");
    }


    @Override
    public List<CellMaterial> getCellMaterial(String cellMtrl) {
        return cellMaterialRepository.findByCELLMTRL(cellMtrl);
    }

    @Override
    public List<CellMaterial> getCellMaterialMaster(String cellMtrl) {
        return cellMaterialRepository.findByCELLMTRLAndType(cellMtrl, CellMaterial.TYPE_M);
    }

    @Override
    public List<CellMaterial> getCellMaterialMaster(List<String> cellMtrlList) {
        return cellMaterialRepository.findByCELLMTRLInAndType(cellMtrlList, CellMaterial.TYPE_M);
    }

    @Override
    public List<Map> getOcMaterial(String product) {
        return cellMaterialRepository.getOcMaterial("57%", product+"%");
    }
}
