package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.key.MrpMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpLcmMaterialRepository extends JpaRepository<MrpLcmMaterial, MrpMaterialKey>, JpaSpecificationExecutor<MrpLcmMaterial> {

    /**
     * 筛选MRP版本
     * @param ver MRP版本
     * @return List
     */
    List<MrpLcmMaterial> findByVer(String ver);

    /**
     * 筛选版本获取料号
     * @param ver MRP版本
     * @return List
     */
    @Query(value = "select m.material from MrpLcmMaterial m where m.ver=:ver")
    List<String> getMaterial(@Param("ver") String ver);
}
