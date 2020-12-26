package com.ivo.mrp.repository.pol;

import com.ivo.mrp.entity.pol.DpsPol;
import com.ivo.mrp.key.DpsPolKey;
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
public interface DpsPolRepository extends JpaRepository<DpsPol, DpsPolKey> {

    List<DpsPol> findByVerAndProduct(String ver, String product);

    @Query(value = "select distinct d.product as product, d.project as project from DpsPol d where d.ver=:ver and d.product like :searchProduct",
            countQuery = "select COUNT(DISTINCT d.product) from DpsPol d where d.ver=:ver and d.product like :searchProduct")
    Page<Map> getPageProduct(@Param("ver") String ver, @Param("searchProduct") String searchProduct, Pageable pageable);

    List<DpsPol> findByVerAndProject(String ver, String project);

    @Query(value = "select distinct product from DpsPol where ver=:ver")
    List<String> getDpsPolProduct(@Param("ver") String ver);
}
