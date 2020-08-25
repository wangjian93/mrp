package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Dps;
import com.ivo.mrp2.key.DpsPrimaryKey;
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
public interface DpsRepository extends JpaRepository<Dps, DpsPrimaryKey> {

    List<Dps> findByDpsVer(String dpsVer);

    List<Dps> findByDpsVerAndProduct(String dpsVer, String product);

    Dps findTopByDpsVer(String dpsVer);

    @Query(value = "select min(fab_date) as startDate, max(fab_date) as endDate from mrp2_dps t where t.dps_ver=:dpsVer", nativeQuery = true)
    Map<String, Date> getDpsDateRange(@Param("dpsVer") String dpsVer);

    @Query(value = "select dps_ver from mrp2_dps group by dps_ver ORDER BY dps_ver DESC", nativeQuery = true)
    List<String> getDpsVer();

    @Query(value = "SELECT DISTINCT product from mrp2_dps t where t.dps_ver=:dpsVer", nativeQuery = true)
    List<String> getProduct(@Param("dpsVer") String dpsVer);

    @Query(value = "select t.dps_ver as dpsVer, t.fab_date as fabDate, t.material as material, t.demand_qty as demandQty, \n" +
            "(select loss_rate from mrp2_material_loss_rate b where b.material=t.material and b.effect_date<=now() and b.expire_date>now()) as lossRate,\n" +
            "(select sum(plan_qty) from mrp2_supplier_arrival_plan a where a.material=t.material and a.date=t.fab_date) as planQty\n" +
            "from \n" +
            "(\n" +
            "select d.dps_ver, d.fab_date, d.material, SUM(d.demand_qty) as demand_qty\n" +
            "from mrp2_demand d where d.dps_ver=:dpsVer \n" +
            "GROUP BY d.dps_ver, d.fab_date, d.material\n" +
            ") t", nativeQuery = true)
    List<Map> summaryMaterial(@Param("dpsVer") String dpsVer);
}
