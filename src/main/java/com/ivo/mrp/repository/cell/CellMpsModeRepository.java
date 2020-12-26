package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.CellMpsMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface CellMpsModeRepository extends JpaRepository<CellMpsMode, String> {

    @Query(value = "select distinct cellInputPc as product from CellMpsMode ")
    List<String> getProduct();

    @Query(value = "select cellMtrl from CellMpsMode where cellInputPc=:product")
    List<String> getCellMtrlByProduct(@Param("product")  String product);

    @Query(value = "select distinct cellInputPc as cellInputPc from CellMpsMode where cellInputPc like :searchProduct",
    countQuery = "select count(distinct cellInputPc) from CellMpsMode where cellInputPc like :searchProduct")
    Page<Map> getPageCellMpsModeProduct(@Param("searchProduct") String searchProduct, Pageable pageable);
}
