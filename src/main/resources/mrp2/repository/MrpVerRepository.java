package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpVer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpVerRepository extends JpaRepository<MrpVer, String>, JpaSpecificationExecutor<MrpVer> {

    int countByDpsVer(String dpsVer);

    @Query(value = "select t.mrp_ver from mrp2_ver t group by t.mrp_ver ORDER BY t.mrp_ver DESC", nativeQuery = true)
    List<String> getMrpVer();

    @Query(value = "select b.product, b.material, b.usage_qty AS usageQty, \n" +
            "(select substitute_rate from mrp2_material_substitute s where s.plant=b.plant and s.product=b.product and s.material=b.material) as substituteRate,\n" +
            "(select slice from mrp2_product_slice l where l.product=b.product) as slice\n" +
            "from mrp2_bom b \n" +
            "where b.plant=:plant \n" +
            "AND b.product=:product ", nativeQuery = true)
    List<Map> sqlHandelDemand(@Param("plant") String plant, @Param("product") String product);


    //TP月结
//    select b.product, b.material, b.material_group, b.usage_qty AS usageQty,
//            (select substitute_rate from mrp2_material_substitute s where s.plant=b.plant and s.product=b.product and s.material=b.material) as substituteRate,
//    (select slice from mrp2_product_slice l where l.product=b.product) as slice
//    from mrp2_bom b
//    where b.plant='LCM2'
//    AND b.product='5133NVFF-001E10'
//    AND b.material_group in (select material_group from mrp2_month_settlement m where m.product=b.product and m.material_group=b.material_group)
}
