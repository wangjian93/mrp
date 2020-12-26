package com.ivo.mrp.repository.pol;

import com.ivo.mrp.entity.pol.DpsCellPolProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsCellPolProductRepository extends JpaRepository<DpsCellPolProduct, Long> {

    List<DpsCellPolProduct> findByVerAndProject(String ver, String project);

}
