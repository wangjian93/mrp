package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.key.DpsKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsLcmRepository extends JpaRepository<DpsLcm, DpsKey> {

    /**
     * 筛选dps版本
     * @param ver dps版本
     * @return List<DpsLcm>
     */
    List<DpsLcm> findByVer(String ver);

    /**
     * 查询DPS中的机种
     * @param ver 版本
     * @return List<String>
     */
    @Query(value = "select distinct d.product from DpsLcm d where d.ver=:ver")
    List<String> getProduct(@Param("ver") String ver);

    /**
     * 筛选dps版本、机种
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsLcm>
     */
    List<DpsLcm> findByVerAndProduct(String ver, String product);

    /**
     * 分页查询DPS的机种
     * @param ver 版本
     * @param searchProduct 机种
     * @param pageable 分页
     * @return Page<Map>
     */
    @Query(value = "select distinct d.product as product, d.project as project, d.fab as fab from DpsLcm d where d.ver=:ver and d.product like :searchProduct",
            countQuery = "select COUNT(DISTINCT d.product) from DpsLcm d where d.ver=:ver and d.product like :searchProduct")
    Page<Map> getPageProduct(@Param("ver") String ver, @Param("searchProduct") String searchProduct, Pageable pageable);

    /**
     * 筛选dps版本、机种、开始日期
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsLcm>
     */
    List<DpsLcm> findByVerAndProductAndFabDateGreaterThanEqual(String ver, String product, Date startDate);
}