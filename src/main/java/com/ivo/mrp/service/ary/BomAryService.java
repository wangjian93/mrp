package com.ivo.mrp.service.ary;

import com.ivo.mrp.entity.direct.ary.BomAry;
import com.ivo.mrp.entity.direct.ary.BomAryProduct;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomAryService {

    /**
     * 同步Ary的MPS命名
     */
    void syncAryMpsMode();

    /**
     * 同步Ary的机种
     */
    void syncBomAryProduct();

    /**
     * 同步Ary的BOM
     */
    void syncBomAry();

    /**
     * 从MPS命名获取机种的cell料号
     * @param product 机种
     * @return List<String>
     */
    List<String> getCellMtrl(String product);

    /**
     * 获取Ary机种的BOM
     * @param product 机种
     * @return List<BomCell
     */
    List<BomAry> getBomAry(String product);

    /**
     * 分页查询cell机种
     * @param page 页数
     * @param limit 分页大小
     * @param searchProduct 查询
     * @return Page<BomCellProduct>
     */
    Page<BomAryProduct> queryProduct(int page, int limit, String searchProduct);

    List<BomAryProduct> getBomAryProduct();

    BomAryProduct getBomAryProduct(String product);

    List<Map> getBomAryOc(String product);
}
