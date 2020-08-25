package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.key.BomPrimaryKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomRepository extends JpaRepository<Bom, BomPrimaryKey> {

    /**
     * 清空表
     */
    @Transactional
    @Modifying
    @Query(value = "truncate table mrp2_bom", nativeQuery = true)
    void truncate();

    /**
     * 查询机种的BOM材料
     * @param plant 厂别
     * @param product 机种
     * @param sort 排序
     * @return List<Bom>
     */
    List<Bom> findByPlantAndProduct(String plant, String product, Sort sort);

    /**
     * 查询机种的BOM材料
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param sort 排序
     * @return List<Bom>
     */
    List<Bom> findByPlantAndProductAndMaterialGroup(String plant, String product, String materialGroup, Sort sort);

    /**
     * 查询机种
     * @param plant 厂别
     * @param product 机种
     * @return List<Map>
     */
    @Query(value = "select product FROM mrp2_bom where plant=:plant and product like :product GROUP BY plant,product ORDER BY product",
            countQuery = "select count(*) FROM mrp2_bom where plant=:plant and product like :product GROUP BY plant,product",
            nativeQuery = true)
    Page<String> searchProduct(@Param("plant") String plant, @Param("product") String product, Pageable pageable);

    /**
     * 获取BOM机种下的物料组
     * @param plant 厂别
     * @param product 机种
     * @return
     */
    @Query(value = "select distinct material_group FROM mrp2_bom where plant=:plant and product=:product ORDER BY material_group",
            nativeQuery = true)
    List<String> getMaterialGroup(@Param("plant") String plant, @Param("product") String product);

    /**
     * 获取BOM物料组下的料号
     * @param plant 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return
     */
    @Query(value = "select distinct material FROM mrp2_bom where plant=:plant and product=:product and material_group=:materialGroup ORDER BY material",
            nativeQuery = true)
    List<String> getMaterial(@Param("plant") String plant, @Param("product") String product, @Param("materialGroup") String materialGroup);
}
