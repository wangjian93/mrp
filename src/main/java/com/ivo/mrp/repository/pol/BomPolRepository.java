package com.ivo.mrp.repository.pol;

import com.ivo.mrp.entity.pol.BomPol;
import com.ivo.mrp.key.BomPolKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomPolRepository extends JpaRepository<BomPol, BomPolKey> {

    /**
     * 分页筛选机种
     * @param searchProduct 机种
     * @param pageable 分页
     * @return  Page<BomPol>
     */
    Page<BomPol> findByProductLike(String searchProduct, Pageable pageable);

    /**
     * 筛选机种
     * @param searchProduct 机种
     * @return List<BomPol>
     */
    List<BomPol> findByProductLike(String searchProduct, Sort sort);

    /**
     * 筛选机种
     * @param product 机种
     * @return List<BomPol>
     */
    List<BomPol> findByProduct(String product, Sort sort);

    @Query(value = "select distinct product from BomPol where product like :searchProduct")
    List<String> getBomPolProduct(@Param("searchProduct") String searchProduct);


}
