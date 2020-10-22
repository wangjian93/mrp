package com.ivo.mrp.repository;

import com.ivo.mrp.entity.packaging.MrpPackageMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageMaterialRepository extends JpaRepository<MrpPackageMaterial, Long> {
}
