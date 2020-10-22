package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.MrpAryMaterial;
import com.ivo.mrp.key.MrpMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpAryMaterialRepository extends JpaRepository<MrpAryMaterial, MrpMaterialKey>, JpaSpecificationExecutor<MrpAryMaterial> {

    /**
     * 筛选MRP版本
     * @param ver MRP版本
     * @return List
     */
    List<MrpAryMaterial> findByVer(String ver);

    /**
     * 筛选版本获取料号
     * @param ver MRP版本
     * @return List
     */
    @Query(value = "select m.material from MrpAryMaterial m where m.ver=:ver")
    List<String> getMaterial(String ver);

}
