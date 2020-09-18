package com.ivo.mrp.repository;

import com.ivo.mrp.entity.packaging.SupplierPackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface SupplierPackageRepository extends JpaRepository<SupplierPackage, Long> {

    /**
     * 分页查询
     * @param month 月份
     * @param searchProject 机种
     * @return Page<SupplierPackage>
     */
    Page<SupplierPackage> findByMonthAndProjectLikeAndValidFlag(String month, String searchProject, boolean validFlag, Pageable pageable);
}
