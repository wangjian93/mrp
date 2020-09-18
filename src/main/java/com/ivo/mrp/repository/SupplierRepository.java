package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface SupplierRepository extends JpaRepository<Supplier, String> {

    /**
     * 分页查询供应商
     * @param searchCode ID
     * @param searchName 供应商名
     * @param searchSname 简称
     * @param pageable 分页
     * @return Page<Supplier>
     */
    Page<Supplier> findBySupplierCodeLikeOrSupplierNameLikeOrSupplierSnameLike(String searchCode, String searchName,
                                                                                 String searchSname, Pageable pageable);
}
