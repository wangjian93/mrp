package com.ivo.mrp.repository.packaging;

import com.ivo.mrp.entity.packaging.DpsCellProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsCellProductRepository extends JpaRepository<DpsCellProduct, Long> {

    List<DpsCellProduct> findByVerAndProduct(String ver, String product);
}
