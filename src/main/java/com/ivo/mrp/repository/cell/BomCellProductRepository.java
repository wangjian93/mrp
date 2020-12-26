package com.ivo.mrp.repository.cell;

import com.ivo.mrp.entity.direct.cell.BomCellProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface BomCellProductRepository extends JpaRepository<BomCellProduct, String> {

    Page<BomCellProduct> findByProductLike(String product, Pageable pageable);
}
