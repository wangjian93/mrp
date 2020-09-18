package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.key.DpsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsLcmRepository extends JpaRepository<DpsLcm, DpsKey> {

    /**
     * 筛选dps版本
     * @param ver dps版本
     * @return List<DpsLcm>
     */
    List<DpsLcm> findByVer(String ver);

    /**
     * 获取DPS中的所有机种
     * @param ver dps版本
     * @return List<String>
     */
    @Query(value = "select distinct d.product from DpsLcm d where d.ver=:ver")
    List<String> getProduct(String ver);

    /**
     * 筛选dps版本、机种
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsLcm>
     */
    List<DpsLcm> findByVerAndProduct(String ver, String product);
}