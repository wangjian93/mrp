package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.CellMpsMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface CellMpsModeRepository extends JpaRepository<CellMpsMode, String> {

    @Query(value = "select distinct cellInput_pc as product from CellMpsMode ")
    List<String> getProduct();

    @Query(value = "select cellMtrl from CellMpsMode where cellInput_pc=:product")
    List<String> getCellMtrlByProduct(String product);
}
