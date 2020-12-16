package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.key.MrpPackageKey;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageRepository extends JpaRepository<MrpPackage, MrpPackageKey> {

    List<MrpPackage> findByVerAndPackageIdAndMaterial(String ver, String packageId, String material);
}
