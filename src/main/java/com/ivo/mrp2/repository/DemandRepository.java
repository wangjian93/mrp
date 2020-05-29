package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.Demand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query(value = "SELECT t.dps_ver as dpsVer, t.fab_date as fabDate, t.material as material, SUM(t.demand_qty) as demandQty from mrp_demand t where t.dps_ver=:dpsVer GROUP BY t.dps_ver, t.fab_date, t.material",
    nativeQuery = true)
    List<Map> summaryMaterial(String dpsVer);

    List<Demand> findByDpsVerAndProduct(String dpsVer, String product);



    @Query(value = "select DISTINCT material from mrp_demand where dps_ver=:dpsVer", nativeQuery = true)
    List<String> getMaterial(String dpsVer);

    List<Demand> findByDpsVerAndMaterial(String dpsVer, String material);
}
