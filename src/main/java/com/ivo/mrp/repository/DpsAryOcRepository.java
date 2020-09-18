package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.DpsAryOc;
import com.ivo.mrp.key.DpsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsAryOcRepository extends JpaRepository<DpsAryOc, DpsKey> {

    /**
     * 筛选dps版本
     * @param ver dps版本
     * @return List<DpsAryOc>
     */
    List<DpsAryOc> findByVer(String ver);

    /**
     * 筛选dps版本、机种
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsAryOc>
     */
    List<DpsAryOc> findByVerAndProduct(String ver, String product);

    /**
     * 获取DPS中的所有机种
     * @param ver 版本
     * @return List<String>
     */
    @Query(value = "select distinct d.product from DpsAryOc d where d.ver=:ver")
    List<String> getProduct(String ver);
}
