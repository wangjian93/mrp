package com.ivo.mrp.repository.lcmPackage;

import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageLcmMaterialRepository extends JpaRepository<MrpPackageLcmMaterial, Long>, JpaSpecificationExecutor<MrpPackageLcmMaterial> {

    List<MrpPackageLcmMaterial> findByVerAndIsAlone(String ver, boolean isAlone);

    List<MrpPackageLcmMaterial> findByVer(String ver);

    MrpPackageLcmMaterial findByVerAndMaterial(String ver, String material);

    MrpPackageLcmMaterial findByVerAndProductsAndMaterial(String ver, String product, String material);
}
