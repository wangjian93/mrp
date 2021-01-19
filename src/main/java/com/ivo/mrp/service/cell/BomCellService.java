package com.ivo.mrp.service.cell;

import com.ivo.mrp.entity.direct.cell.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellService {

    /**
     * 同步CELL的MPS命名
     */
    void syncCellMpsMode();

    /**
     * 同步CELL的机种
     */
    void syncBomCellProduct();

    /**
     * 同步CELL的BOM
     */
    void syncBomCell();

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
    List<BomCell> getBomCell(String product);

    /**
     * 分页查询cell机种
     * @param page 页数
     * @param limit 分页大小
     * @param searchProduct 查询
     * @return Page<BomCellProduct>
     */
    Page<BomCellProduct> queryProduct(int page, int limit, String searchProduct);

    List<BomCellProduct> getBomCellProduct();

    BomCellProduct getBomCellProduct(String product);

    void deleteBomCell(List<BomCell> list);

    void saveBomCell(List<BomCell> list);
}
