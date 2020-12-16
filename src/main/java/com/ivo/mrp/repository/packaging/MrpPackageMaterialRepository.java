package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.MrpPackageMaterial;
import com.ivo.mrp.key.MrpPackageMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageMaterialRepository extends JpaRepository<MrpPackageMaterial, MrpPackageMaterialKey>, JpaSpecificationExecutor<MrpPackageMaterial> {
}
