package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.key.MrpDataPrimaryKey;
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
public interface MrpDataRepository extends JpaRepository<MrpData, MrpDataPrimaryKey> {

    List<MrpData> findByMrpVer(String mrpVer);

    List<MrpData> findByMrpVerAndMaterial(String mrpVer, String material);

    List<MrpData> findByMrpVerAndMaterialIn(String mrpVer, List<String> materials);

    MrpData findByMrpVerAndFabDateAndMaterial(String mrpVer, Date fabDate, String material);

    @Query(value = "select DISTINCT material from mrp2_data t where t.mrp_ver=:mrpVer", nativeQuery = true)
    List<String> getMaterial(@Param("mrpVer") String mrpVer);

    @Query(value = "select DISTINCT material from mrp2_data t where t.mrp_ver=:mrpVer",
            countQuery = "select count(DISTINCT material) from mrp2_data t where t.mrp_ver=:mrpVer",
            nativeQuery = true)
    Page<String> getMaterial(@Param("mrpVer") String mrpVer, Pageable pageable);

    @Query(value = "select DISTINCT material from mrp2_demand t where t.dps_ver = (select dps_ver from mrp2_ver where mrp_ver=:mrpVer) and product =:product",
            countQuery = "select count(DISTINCT material) from mrp2_demand t where t.dps_ver = (select dps_ver from mrp2_ver where mrp_ver=:mrpVer) and product =:product",
            nativeQuery = true)
    Page<String> getMaterialByProduct(@Param("mrpVer") String mrpVer, @Param("product") String product, Pageable pageable);



    List<MrpData> findByMrpVerAndShortQtyLessThanAndMaterialIn(String mrpVer, double less, List<String> materialList);

    List<MrpData> findByMrpVerAndMaterialInAndFabDateGreaterThanEqualAndFabDateLessThanEqual(String mrpVer, List<String> materialList, Date startDate, Date endDate);

//
//
//    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer and t.products like :product and t.material=:material",
//            countQuery = "select count(DISTINCT material) from mrp_data t where t.mrp_ver=:mrpVer and t.products like :product and t.material=:material",
//            nativeQuery = true)
//    Page<String> getMaterial(String mrpVer, String product, String material, Pageable pageable);
//
//    @Query(value = "select DISTINCT material from mrp_data t where t.mrp_ver=:mrpVer and t.material=:material",
//            countQuery = "select count(DISTINCT material) from mrp_data t where t.mrp_ver=:mrpVer and t.material=:material",
//            nativeQuery = true)
//    Page<String> getMaterialByMaterial(String mrpVer, String material, Pageable pageable);


    @Query(value = "select " +
            "m.material_group, " +
            "sum(cast(p.RQuantity as decimal(18,0))) as rQty, " +
            "sum(cast(p.OrderQty as decimal(18,0))) as orderQty, " +
            "sum(cast(p.Quantity as decimal(18,0))) as qty " +
            "from mrp2_openprpo p " +
            "left join mrp2_material m on m.material=p.Material_FK " +
            "where m.material_group=:materialGroup and p.DateOfClose like :month and p.DataType='PR' " +
            "GROUP BY m.material_group",
            nativeQuery = true)
    Map getOpenPr(@Param("month") String month, @Param("materialGroup") String materialGroup);

    @Query(value = "select " +
            "m.material_group, " +
            "sum(cast(p.RQuantity as decimal(18,0))) as rQty, " +
            "sum(cast(p.OrderQty as decimal(18,0))) as orderQty, " +
            "sum(cast(p.Quantity as decimal(18,0))) as qty " +
            "from mrp2_openprpo p " +
            "left join mrp2_material m on m.material=p.Material_FK " +
            "where m.material_group=:materialGroup and p.DateOfClose like :month and p.DataType='PO' " +
            "GROUP BY m.material_group",
            nativeQuery = true)
    Map getOpenPo(@Param("month") String month, @Param("materialGroup") String materialGroup);

    @Query(value = "SELECT ttt.material_group as materialGroup, ttt.month,  " +
            "SUM(cast(ttt.demand_qty as decimal(18,0))) as demandQty, " +
            "SUM(cast(ttt.good_inventory as decimal(18,0))) as goodInventory, " +
            "SUM(cast(ttt.dull_inventory as decimal(18,0))) as dullInventory, " +
            "(select DISTINCT g.material_group_name from mrp2_material_group g where g.material_group=ttt.material_group) as materialGroupName " +
            "from ( " +
            " SELECT tt.material, tt.month,tt.demand_qty, m.material_group, m.good_inventory, m.dull_inventory from ( " +
            "  SELECT t.material, t.month, SUM(t.demand_qty) as demand_qty from  ( " +
            "   SELECT d.material, DATE_FORMAT(d.fab_date,'%Y-%m') as month, d.demand_qty from mrp2_data d where d.mrp_ver=:mrpVer and d.demand_qty<>0 " +
            "  ) t  " +
            "  GROUP BY t.material, t.month " +
            " ) tt " +
            " LEFT JOIN mrp2_data_master m on m.material=tt.material and m.mrp_ver=:mrpVer " +
            ") ttt " +
            "where ttt.material_group like :materialGroup " +
            "GROUP BY ttt.material_group, ttt.month", nativeQuery = true)
    List<Map> getSumMaterialGroup(@Param("mrpVer") String mrpVer, @Param("materialGroup") String materialGroup);
}
