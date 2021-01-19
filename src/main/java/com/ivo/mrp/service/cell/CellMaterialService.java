package com.ivo.mrp.service.cell;

import com.ivo.mrp.entity.direct.cell.CellMaterial;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface CellMaterialService {

    /**
     * 同步CELL料号的材料
     */
    void syncCellMaterial();

    List<CellMaterial> getCellMaterial(String cellMtrl);

    List<CellMaterial> getCellMaterialMaster(String cellMtrl);

    List<CellMaterial> getCellMaterialMaster(List<String> cellMtrlList);

    List<Map> getOcMaterial(String product);
}
