package com.ivo.mrp.repository.ary;

import com.ivo.mrp.entity.direct.ary.BomAry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomAryRepository extends JpaRepository<BomAry, String> {

    List<BomAry> findByProduct(String product);

}
