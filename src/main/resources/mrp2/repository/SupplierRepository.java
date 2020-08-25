package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author wj
 * @version 1.0
 */
public interface SupplierRepository extends JpaRepository<Supplier, String> {

    /**
     * 按名称模糊查询供应商
     * @param supplier 供应商名
     * @return List<Supplier>
     */
    Page<Supplier> findByNameLike(String supplier, Pageable pageable);
}
