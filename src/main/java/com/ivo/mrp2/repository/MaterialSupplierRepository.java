package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialSupplierRepository extends JpaRepository<MaterialSupplier, Long> {

    Page<MaterialSupplier> findByMaterialLikeAndSupplierCodeLike(String material, String supplierCode, Pageable pageable);

    List<MaterialSupplier> findByMaterial(String material);


    @Query(value = "select supplier from mrp_material_supplier where supplier_code=:supplerCode LIMIT 1", nativeQuery = true)
    String getSupplerName(String supplerCode);
}
