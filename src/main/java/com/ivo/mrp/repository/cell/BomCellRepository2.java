package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.BomCell2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellRepository2 extends JpaRepository<BomCell2, String> {

    List<BomCell2> findByProduct(String product);
}
