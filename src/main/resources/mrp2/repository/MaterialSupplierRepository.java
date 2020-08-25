package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialSupplier;
import com.ivo.mrp2.entity.Supplier;
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
public interface MaterialSupplierRepository extends JpaRepository<MaterialSupplier, Long> {

    /**
     * 查询料号与供应商关系
     * @param material 料号
     * @param supplierCode  供应商ID
     * @return MaterialSupplier
     */
    MaterialSupplier findFirstByMaterialAndSupplierCode(String material, String supplierCode);

    /**
     * 获取材料的所有供应商
     * @param material 料号
     * @return List<String>
     */
    @Query(value = "select new com.ivo.mrp2.entity.Supplier(s.id, s.name, s.sName) from MaterialSupplier m left join Supplier s on m.supplierCode=s.id where m.material=:material")
    List<Supplier> getSupplierByMaterial(@Param("material") String material);


    /**
     * 分页查询料号与供应商信息
     * @param material 料号
     * @param supplier 供应商名字
     * @param pageable 分页
     * @return List<Map>
     */
    @Query(value = "select m.material, a.material_name as materialName,a.material_group as materialGroup, s.id as supplierCode, s.name as supplier, s.s_name as sName from mrp2_material_supplier m " +
            "left join mrp2_supplier s on m.supplier_code=s.id " +
            "left join mrp2_material a on m.material=a.material " +
            "where m.material like :material and s.name like :supplier",
            countQuery = "select count(*) from mrp2_material_supplier m " +
                    "left join mrp2_supplier s on m.supplier_code=s.id " +
                    "left join mrp2_material a on m.material=a.material " +
                    "where m.material like :material and s.name like :supplier",nativeQuery = true)
    Page<Map> getPageMaterialSupplier(@Param("material") String material, @Param("supplier") String supplier, Pageable pageable);

}
