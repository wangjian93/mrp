package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.key.DpsPackageKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsPackageRepository extends JpaRepository<DpsPackage, DpsPackageKey> {

    @Query(value = "select distinct packageId from DpsPackage where ver in :dpsVers ")
    List<String> getPackageId(@Param("dpsVers") List<String> dpsVers);

    List<DpsPackage> findByVerInAndPackageId(List<String> dpsVer, String packageId);
}
