package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.BomLcm;
import com.ivo.mrp.key.MaterialKey;
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
public interface BomLcmRepository extends JpaRepository<BomLcm, MaterialKey> {

    /**
     * 筛选厂别、机种
     * @param fab 厂别
     * @param product 机种
     * @return  List<BomLcm>
     */
    List<BomLcm> findByFabAndProduct(String fab, String product);

    /**
     * 筛选机种
     * @param fab 厂别
     * @param searchProduct 查询机种条件
     * @param pageable 分页
     * @return Page<Map>
     */
    @Query(value = "select DISTINCT b.product AS product from BomLcm b where b.fab=:fab and b.product like :searchProduct",
        countQuery = "select COUNT(DISTINCT b.product) from BomLcm b where b.fab=:fab and b.product like :searchProduct")
    Page<Map> queryProduct(@Param("fab") String fab, @Param("searchProduct") String searchProduct, Pageable pageable);
}
