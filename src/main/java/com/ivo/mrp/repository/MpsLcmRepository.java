package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.MpsLcm;
import com.ivo.mrp.key.MpsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MpsLcmRepository extends JpaRepository<MpsLcm, MpsKey> {

    /**
     * 获取MPS中的所有机种
     * @param ver 版本
     * @return List<String>
     */
    @Query(value = "select distinct m.product from MpsLcm m where m.ver=:ver")
    List<String> getProduct(@Param("ver") String ver);

    /**
     * 筛选版本、机种
     * @param ver MPS版本
     * @param product 机种
     * @return List<MpsLcm>
     */
    List<MpsLcm> findByVerAndProduct(String ver, String product);

    /**
     * 筛选版本
     * @param ver MPS版本
     * @return List<MpsLcm>
     */
    List<MpsLcm> findByVer(String ver);

    /**
     * 筛选版本、机种、日期
     * @param ver MPS版本
     * @param product 机种
     * @return List<MpsLcm>
     */
    List<MpsLcm> findByVerAndProductAndFabDateGreaterThanEqual(String ver, String product, Date startDate);
}
