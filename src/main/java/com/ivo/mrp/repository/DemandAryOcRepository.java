package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.DemandAryOc;
import com.ivo.mrp.key.DemandKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface DemandAryOcRepository extends JpaRepository<DemandAryOc, DemandKey> {

    /**
     * 筛选MRP版本、料号、日期
     * @param ver 版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<DemandLcm>
     */
    List<DemandAryOc> findByVerAndMaterialAndFabDate(String ver, String material, Date fabDate);

    /**
     * 获取料号的需求机种
     * @param ver MRP版本
     * @return List<String>
     */
    @Query(value = "select DISTINCT d.product from DemandAryOc d where d.ver=:ver and d.material=:material")
    List<String> getProduct(@Param("ver") String ver, @Param("material") String material);

    List<DemandAryOc> findByVer(String ver);
}
