package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.BomPackageMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomPackageMaterialRepository extends JpaRepository<BomPackageMaterial, Long> {

    List<BomPackageMaterial> findByPackageId(String packageId);

    List<BomPackageMaterial> findByPackageIdAndSupplierFlagIsTrue(String packageId);

}
