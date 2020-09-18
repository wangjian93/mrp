package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.MrpAry;
import com.ivo.mrp.entity.direct.cell.MrpCell;
import com.ivo.mrp.key.MrpKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpCellRepository extends JpaRepository<MrpCell, MrpKey> {

    /**
     * 筛选版本、料号
     * @param ver MRP版本
     * @param material 料号
     * @return List<MrpLcm>
     */
    List<MrpCell> findByVerAndMaterialOrderByFabDateAsc(String ver, String material);

    /**
     * 筛选版本
     * @param ver MRP版本
     * @return List<MrpLcm>
     */
    List<MrpCell> findByVerOrderByFabDateAsc(String ver);
}
