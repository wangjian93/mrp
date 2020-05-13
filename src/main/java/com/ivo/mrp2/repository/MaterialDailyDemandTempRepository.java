package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialDailyDemandTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialDailyDemandTempRepository extends JpaRepository<MaterialDailyDemandTemp, Long> {

    @Query(value = "select mrp_ver as mrpVer, fab_Date as fabDate, material, material_name as materialName, sum(demand_qty) as demandQty " +
            "from mrp2_material_daily_demand_loss_temp t where t.mrp_ver=:mrpVer GROUP BY mrp_ver, fab_Date, material, material_name", nativeQuery = true)
    List<Map> summaryMaterialDemandTemp(String mrpVer);

    List<MaterialDailyDemandTemp> findByMrpVer(String mrpVer);

    List<MaterialDailyDemandTemp> findByMrpVerAndMaterial(String mrpVer, String material);
}
