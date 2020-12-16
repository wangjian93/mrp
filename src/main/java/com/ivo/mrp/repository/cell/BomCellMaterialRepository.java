package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.BomCellMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellMaterialRepository extends JpaRepository<BomCellMaterial, Long> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE MRP3_Bom_Cell_Material", nativeQuery = true)
    void truncateTable();

    List<BomCellMaterial> findByCELLMTRLIn(List<String> cellMtrlList);
}
