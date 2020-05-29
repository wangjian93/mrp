package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialSupplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialSupplierRepository extends JpaRepository<MaterialSupplier, Long> {

    Page<MaterialSupplier> findByMaterialLikeAndSupplierCodeLike(String material, String supplierCode, Pageable pageable);
}
