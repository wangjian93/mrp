package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.key.ArrivalPlanKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query(value = "SELECT * from ( " +
            "select DISTINCT a.material, a.supplier_code as supplierCode, a.supplier_sname as supplierSname, m.material_name as materialName, m.material_group as materialGroup from mrp3_arrival_plan a " +
            "LEFT JOIN mrp3_material m on m.material=a.material " +
            "where a.fab like :fab and a.fab_date >= :startDate and a.fab_date <= :endDate " +
            "and a.material like :searchMaterial " +
            "and a.supplier_code like :searchSupplierCode  " +
            "and m.material_group like :searchMaterialGroup " +
            "UNION " +
            "select DISTINCT b.material, b.supplier_code as supplierCode, b.supplier_sname as supplierSname, m.material_name as materialName, m.material_group as materialGroup from mrp3_allocation b " +
            "LEFT JOIN mrp3_material m on m.material=b.material " +
            "where b.fab like :fab and b.fab_date >= :startDate and b.fab_date <= :endDate " +
            "and b.material like :searchMaterial " +
            "and b.supplier_code like :searchSupplierCode  " +
            "and m.material_group like :searchMaterialGroup " +
            ") t ORDER BY material,supplierCode",
            countQuery = "SELECT COUNT(*) from ( " +
                    "select DISTINCT a.material, a.supplier_code from mrp3_arrival_plan a " +
                    "LEFT JOIN mrp3_material m on m.material=a.material " +
                    "where a.fab like :fab and a.fab_date >= :startDate and a.fab_date <= :endDate " +
                    "and a.material like :searchMaterial " +
                    "and a.supplier_code like :searchSupplierCode  " +
                    "and m.material_group like :searchMaterialGroup " +
                    "UNION " +
                    "select DISTINCT b.material, b.supplier_code from mrp3_allocation b " +
                    "LEFT JOIN mrp3_material m on m.material=b.material " +
                    "where b.fab like :fab and b.fab_date >= :startDate and b.fab_date <= :endDate " +
                    "and b.material like :searchMaterial " +
                    "and b.supplier_code like :searchSupplierCode  " +
                    "and m.material_group like :searchMaterialGroup" +
                    ") t ",
            nativeQuery = true
    )
    Page<Map> getPageArrivalPlanMaterial(Date startDate, Date endDate,
            String fab, String searchMaterialGroup, String searchMaterial, String searchSupplierCode, Pageable pageable);

    List<ArrivalPlan> findByFabAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
            String fab, String material, String supplierCode, Date startDate, Date endDate
    );
}
