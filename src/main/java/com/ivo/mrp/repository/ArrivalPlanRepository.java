package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.key.ArrivalPlanKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface ArrivalPlanRepository extends JpaRepository<ArrivalPlan, ArrivalPlanKey> {

    /**
     * 获取总的到货量
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return Double
     */
    @Query(value = "select sum(a.arrivalQty) from ArrivalPlan a where a.fab=:fab and a.material=:material and a.fabDate=:fabDate ")
    Double getArrivalPlanQty(String fab, String material, Date fabDate);

    /**
     * 获取供应商到货计划，筛选厂别，料号，日期
     * @param fab 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return List<ArrivalPlan>
     */
    List<ArrivalPlan> findByFabAndMaterialAndFabDate(String fab, String material, Date fabDate);

    /**
     * 获取总的到货量
     * @param fab 厂别
     * @param material 料号
     * @param fabDateList 日期集合
     * @return List<Map
     */
    @Query(value = "select a.fabDate as fabDate, sum(a.arrivalQty) as arrivalQty from ArrivalPlan a where a.fab=:fab and a.material=:material and a.fabDate in :fabDateList group by a.fabDate")
    List<Map> getArrivalPlan(String fab, String material, List<Date> fabDateList);
}
