package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.lcm.DemandLcm;
import com.ivo.mrp.key.DemandKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DemandLcmRepository extends JpaRepository<DemandLcm, DemandKey> {

    /**
     * 获取MRP需求所有的料号
     * @param ver MRP版本
     * @return List<String>
     */
    @Query(value = "select DISTINCT d.material from DemandLcm d where d.ver=:ver")
    List<String> getMaterial(String ver);

    /**
     * 汇总料号需求
     * @param ver MRP版本
     * @param material 料号
     * @return List<Map<Date, Double>>
     */
    @Query(value = "select d.fabDate as fabDate, sum(d.demandQty) as demandQty from DemandLcm d where d.ver=:ver and d.material=:material and d.demandQty>0 group by d.fabDate")
    List<Map> getDemandQtyLcm(String ver, String material);

    /**
     * 汇总料号、日期需求
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    @Query(value = "select sum(d.demandQty) as demandQty from DemandLcm d where d.ver=:ver and d.material=:material and d.demandQty>0 and d.fabDate=:fabDate")
    Double getDemandQtyLcm(String ver, String material, Date fabDate);
}
