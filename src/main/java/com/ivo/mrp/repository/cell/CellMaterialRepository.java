package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.CellMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface CellMaterialRepository extends JpaRepository<CellMaterial, Long> {

    @Transactional
    @Modifying
    @Query(value = "TRUNCATE TABLE MRP3_Bom_Cell_Material", nativeQuery = true)
    void truncateTable();

    List<CellMaterial> findByCELLMTRL(String cellMtrl);

    List<CellMaterial> findByCELLMTRLAndType(String cellMtrl, String type);

    List<CellMaterial> findByCELLMTRLInAndType(List<String> cellMtrlList, String type);

    @Query("SELECT MTRL_ID as material, materialGroup as materialGroup, materialName as materialName, materialGroupName as materialGroupName, USAGEQTY as usageQty FROM CellMaterial WHERE MTRL_ID LIKE :material AND PRODUCT LIKE :product")
    List<Map> getOcMaterial(@Param("material") String material, @Param("product") String product);
}
