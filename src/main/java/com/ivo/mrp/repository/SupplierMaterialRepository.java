package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.SupplierMaterial;
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
public interface SupplierMaterialRepository extends JpaRepository<SupplierMaterial, Long> {

    /**
     * 筛选料号
     * @param material 料号
     * @param validFlag 有效性标识
     * @return List<SupplierMaterial>
     */
    List<SupplierMaterial> findByMaterialAndValidFlag(String material, boolean validFlag);

    /**
     * 筛选供应商、料号
     * @param supplierCode 供应商
     * @param material 料号
     * @param validFlag 有效性标识
     * @return SupplierMaterial
     */
    SupplierMaterial findBySupplierCodeAndMaterialAndValidFlag(String supplierCode, String material, boolean validFlag);

    /**
     * 分页查询材料与供应商
     * @param searchMaterial 料号查询
     * @param searchSupplier 供应商查询
     * @param pageable 分页
     * @return Page<Map>
     */
    @Query(value = "select m.material as material, m.material_name as materialName, m.material_group as materialGroup, " +
            "s.supplier_code as supplierCode, s.supplier_name as supplierName, s.supplier_sname as supplierSname " +
            "from mrp3_supplier_material r " +
            "LEFT JOIN  mrp3_supplier s on s.supplier_code=r.supplier_code " +
            "LEFT JOIN mrp3_material m on m.material=r.material " +
            "where " +
            "r.valid_flag=1 " +
            "and m.material like :searchMaterial and (s.supplier_code like :searchSupplier or s.supplier_name like :searchSupplier or s.supplier_sname like :searchSupplier) " +
            "ORDER BY m.material,s.supplier_code",
            countQuery = "select COUNT(*) " +
                    "from mrp3_supplier_material r " +
                    "LEFT JOIN  mrp3_supplier s on s.supplier_code=r.supplier_code " +
                    "LEFT JOIN mrp3_material m on m.material=r.material " +
                    "where " +
                    "r.valid_flag=1 " +
                    "and m.material like :searchMaterial and (s.supplier_code like :searchSupplier or s.supplier_name like :searchSupplier or s.supplier_sname like :searchSupplier)",
            nativeQuery = true)
    Page<Map> querySupplierMaterial(@Param("searchMaterial") String searchMaterial, @Param("searchSupplier") String searchSupplier, Pageable pageable);
}
