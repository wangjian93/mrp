package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.BomLcm;
import com.ivo.mrp.key.MaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomLcmRepository extends JpaRepository<BomLcm, MaterialKey> {

    /**
     * 筛选厂别、机种
     * @param fab 厂别
     * @param product 机种
     * @return  List<BomLcm>
     */
    List<BomLcm> findByFabAndProduct(String fab, String product);
}
