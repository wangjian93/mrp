package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.PackageAllocation;
import com.ivo.mrp.key.PackageAllocationKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface PackageAllocationRepository extends JpaRepository<PackageAllocation, PackageAllocationKey> {
}
