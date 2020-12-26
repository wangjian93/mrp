package com.ivo.mrp.service.cell;

import com.ivo.mrp.entity.direct.cell.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellService {

    /**
     * 同步CELL料号的B材料
     */
    void syncBomCellMaterial();

    /**
     * 同步CELL的MPS命名
     */
    void syncCellMpsMode();

    /**
     * 同步CELL的机种
     */
    void syncBomCellProduct();

    void syncBomCell2();


    /**
     * 获取CELL料号的材料
     * @param cellMtrlList cell料号集合
     * @return  List<BomCellMaterial>
     */
    List<BomCellMaterial> getBomCellMaterial(List<String> cellMtrlList);

    /**
     * 从MPS命名获取机种的cell料号
     * @param product 机种
     * @return List<String>
     */
    List<String> getCellMtrl(String product);

    /**
     * 获取CELL机种的BOM
     * @param product 机种
     * @return List<BomCell
     */
    List<BomCell2> getBomCell(String product);

    Page<BomCellProduct> queryProduct(int page, int limit, String searchProduct);


    void deleteBomCell(List<BomCell2> list);
    void saveBomCell(List<BomCell2> list);
}
