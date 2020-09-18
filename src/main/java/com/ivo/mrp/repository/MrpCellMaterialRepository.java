package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.cell.MrpCellMaterial;
import com.ivo.mrp.key.MrpMaterialKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpCellMaterialRepository extends JpaRepository<MrpCellMaterial, MrpMaterialKey> {

    /**
     * 筛选MRP版本
     * @param ver MRP版本
     * @return List
     */
    List<MrpCellMaterial> findByVer(String ver);

    /**
     * 筛选版本获取料号
     * @param ver MRP版本
     * @return List
     */
    @Query(value = "select m.material from MrpCellMaterial m where m.ver=:ver")
    List<String> getMaterial(String ver);
}
