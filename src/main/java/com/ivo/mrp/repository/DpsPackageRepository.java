package com.ivo.mrp.repository;

import com.ivo.mrp.entity.packaging.DpsPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsPackageRepository extends JpaRepository<DpsPackage, Long> {

    /**
     * 筛选dps版本
     * @param ver dps版本
     * @return  List<DpsPackage>
     */
    List<DpsPackage> findByVer(String ver);

    /**
     * 获取DPS中的所有机种
     * @param ver 版本
     * @return List<String>
     */
    @Query(value = "select d.product as product, d.type as type, d.linkQty as linkQty, d.mode as mode from DpsPackage d where d.ver=:ver group by d.product, d.type, d.linkQty, d.mode")
    List<Map> getProduct(@Param("ver") String ver);

    /**
     * 筛选dps版本、机种
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsLcm>
     */
    List<DpsPackage> findByVerAndProductAndTypeAndLinkQtyAndMode(String ver, String product, String type, Double linkQty, String mode);
}
