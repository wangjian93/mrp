package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.cell.BomCell;
import com.ivo.mrp.key.BomCellKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellRepository extends JpaRepository<BomCell, BomCellKey> {

    /**
     * 筛选机种
     * @param fab 厂别
     * @param searchProduct 查询机种条件
     * @param pageable 分页
     * @return Page<Map>
     */
    @Query(value = "select DISTINCT b.product AS product from BomCell b where b.fab=:fab and b.product like :searchProduct",
            countQuery = "select COUNT(DISTINCT b.product) from BomCell b where b.fab=:fab and b.product like :searchProduct")
    Page<Map> queryProduct(@Param("fab") String fab, @Param("searchProduct") String searchProduct, Pageable pageable);

    /**
     * 筛选机种
     * @param product 机种
     * @return List<Map>
     */
    @Query(value = "SELECT " +
            "b.fab as fab, b.project as project, b.product as product, " +
            "b.cell_mtrl as cellMtrl, b.material_group as materialGroup, " +
            "b.material_name as materialName, b.material_group_name as materialGroupName, " +
            "b.use_flag as useFlag, '' as material, '' as measureUnit, '' as usageQty " +
            "FROM mrp3_bom_cell b where product=:product " +
            "UNION " +
            "SELECT " +
            "b.fab as fab, b.project as project, b.product as product, " +
            "b.cell_mtrl as cellMtrl, b.material_group as materialGroup, " +
            "b.material_name as materialName, b.material_group_name as materialGroupName, " +
            "b.use_flag as useFlag, b.material as material, b.measure_unit as measureUnit, b.usage_qty as usageQty " +
            "FROM mrp3_bom_cell_mtrl b where product=:product", nativeQuery = true)
    List<Map> findByProduct(@Param("product") String product);
}
