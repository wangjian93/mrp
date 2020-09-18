package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.cell.BomCell;
import com.ivo.mrp.key.BomCellKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellRepository extends JpaRepository<BomCell, BomCellKey> {
}
