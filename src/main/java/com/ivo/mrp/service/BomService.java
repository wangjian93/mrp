package com.ivo.mrp.service;

import com.ivo.mrp.entity.direct.ary.BomAry;
import com.ivo.mrp.entity.direct.cell.BomCellMtrl;
import com.ivo.mrp.entity.direct.lcm.BomLcm;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * BOM的服务接口
 * @author wj
 * @version 1.0
 */
public interface BomService {

    /**
     * 同步LCM的BOM
     */
    void syncBomLcm();

    /**
     * 同步CELL的BOM
     */
    void syncBomCell();

    /**
     * 同步ARY的BOM
     */
    void syncBomAry();

    /**
     * 获取LCM的机种BOM
     * @param product 机种
     * @Param fab 厂别
     * @return List<BomLcm>
     */
    List<BomLcm> getLcmBom(String product, String fab);

    /**
     * 获取ARY的机种BOM
     * @param product 机种
     * @return List<BomLcm>
     */
    List<BomAry> getAryBom(String product);

    /**
     * 获取ARY二次Input时OC光刻胶材料的BOM
     * 从CELL的BOM List中抓取光刻胶112物料组
     * @param product 机种
     * @return List<Map>
     */
    List<Map> getAryOcBom(String product);

    /**
     * 获取CELL的机种BOM
     * @param product 机种
     * @return List<BomLcm>
     */
    List<BomCellMtrl> getCellBom(String product);

    /**
     * 主材查询BOM的机种
     * @param page 页数
     * @param limit 分页大小
     * @param fab 厂别
     * @param searchProduct 机种查询
     * @return Page<Map>
     */
    Page<Map> queryProduct(int page, int limit, String fab, String searchProduct);

    /**
     * 获取去CELL的机种BOM
     * @param product 机种
     * @return List<Map>
     */
    List<Map> queryCellBom(String product);
}
