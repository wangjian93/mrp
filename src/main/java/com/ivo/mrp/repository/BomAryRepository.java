package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.BomAry;
import com.ivo.mrp.key.MaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomAryRepository extends JpaRepository<BomAry, MaterialKey> {

    /**
     * 筛选机种
     * @param product 机种
     * @return  List<BomLcm>
     */
    List<BomAry> findByProduct(String product);

    /**
     * 获取ARY二次Input时OC光刻胶材料的BOM
     * @return List<Map>
     */
    @Query(value = "select material, usage_qty as usageQty from mrp3_bom_cell_mtrl where project=:product and material_group='112' ORDER BY material limit 1",
    nativeQuery = true)
    List<Map> getAryOcBom(String product);
}
