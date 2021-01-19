package com.ivo.mrp.repository.ary;

import com.ivo.mrp.entity.direct.ary.BomAryProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wj
 * @version 1.0
 */
public interface BomAryProductRepository extends JpaRepository<BomAryProduct, String> {

    Page<BomAryProduct> findByProductLike(String product, Pageable pageable);

}
