package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.MpsAry;
import com.ivo.mrp.key.MpsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MpsAryRepository extends JpaRepository<MpsAry, MpsKey> {

    /**
     * 获取MPS中的所有机种
     * @param ver 版本
     * @return List<String>
     */
    @Query(value = "select distinct m.product from MpsAry m where m.ver=:ver")
    List<String> getProduct(String ver);

    /**
     * 筛选版本、机种
     * @param ver MPS版本
     * @param product 机种
     * @return List<MpsLcm>
     */
    List<MpsAry> findByVerAndProduct(String ver, String product);

    /**
     * 筛选版本
     * @param ver MPS版本
     * @return List<MpsLcm>
     */
    List<MpsAry> findByVer(String ver);
}
