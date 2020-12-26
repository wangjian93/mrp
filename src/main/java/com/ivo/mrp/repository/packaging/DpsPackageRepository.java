package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.key.DpsPackageKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsPackageRepository extends JpaRepository<DpsPackage, DpsPackageKey> {

    @Query(value = "select distinct packageId from DpsPackage where ver in :dpsVers ")
    List<String> getPackageId(@Param("dpsVers") List<String> dpsVers);

    List<DpsPackage> findByVerInAndPackageId(List<String> dpsVer, String packageId);

    List<DpsPackage> findByVerAndPackageId(String dpsVer, String packageId);

    @Query(value = "select distinct d.packageId as packageId, d.product as product, d.type as type, d.linkQty as linkQty from DpsPackage d where d.ver=:ver and d.packageId like :searchProduct",
            countQuery = "select COUNT(DISTINCT d.packageId) from DpsPackage d where d.ver=:ver and d.packageId like :searchProduct")
    Page<Map> getPagePackageId(@Param("ver") String ver, @Param("searchProduct") String searchProduct, Pageable pageable);

    List<DpsPackage> findByVerAndProduct(String ver, String product);
}
