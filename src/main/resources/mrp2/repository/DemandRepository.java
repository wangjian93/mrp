package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Demand;
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
public interface DemandRepository extends JpaRepository<Demand, Long> {

    List<Demand> findByDpsVerAndFabDateAndMaterial(String dpsVer, Date fabDate, String material);

    Demand findByDpsVerAndMaterialAndFabDate(String mrpVer, String material, Date fabDate);

    List<Demand> findByDpsVer(String dpsVer);

    @Query(value = "SELECT t.dps_ver as dpsVer, t.fab_date as fabDate, t.material as material, SUM(t.demand_qty) as demandQty from mrp2_demand t where t.dps_ver=:dpsVer GROUP BY t.dps_ver, t.fab_date, t.material",
    nativeQuery = true)
    List<Map> summaryMaterial(@Param("dpsVer") String dpsVer);

    List<Demand> findByDpsVerAndProduct(String dpsVer, String product);



    @Query(value = "select DISTINCT material from mrp2_demand where dps_ver=:dpsVer", nativeQuery = true)
    List<String> getMaterial(@Param("dpsVer") String dpsVer);

    List<Demand> findByDpsVerAndMaterial(String dpsVer, String material);

    Demand findFirstByDpsVer(String dpsVer);


    @Query(value = "select d.fab_date as fabDate, SUM(cast(d.demand_qty as  decimal(18,4))) as demandQty " +
            "from mrp2_demand d " +
            "where d.dps_ver in :dpsVers " +
            "and d.material=:material " +
            "GROUP BY material,fab_date ", nativeQuery = true)
    List<Map> sumMaterialDemand(@Param("dpsVers") List<String> dpsVers, @Param("material") String material);

    @Query(value = "select material, group_concat(distinct d.product SEPARATOR ',') as product from mrp2_demand d " +
            "where d.dps_ver in :dpsVers " +
            "GROUP BY material", nativeQuery = true)
    List<Map> sumMaterial(@Param("dpsVers") List<String> dpsVers);
}
