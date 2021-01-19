package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.BomCell;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellRepository extends JpaRepository<BomCell, String> {

    List<BomCell> findByProduct(String product);
}
