package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.key.SupplierArrivalPlanKey;
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
public interface SupplierArrivalPlanRepository extends JpaRepository<SupplierArrivalPlan, SupplierArrivalPlanKey> {

    @Query(value = "select sum(arrival_qty) from mrp2_supplier_arrival_plan where plant=:plant and material=:material and date=:date", nativeQuery = true)
    Double getMaterialArrivalPlanQty(@Param("plant") String plant, @Param("material") String material, @Param("date") Date date);

    List<SupplierArrivalPlan> findByPlantAndMaterialAndDate(String plant, String material, Date fabDate);

    @Query(value = "select a.plant, a.supplier as supplierCode, a.material  from mrp2_supplier_arrival_plan a " +
            "where a.date>=:startDate and a.date <:endDate " +
            "and a.material like :material and a.supplier like :supplier and a.plant like :plant " +
            "GROUP BY a.supplier, a.material, a.plant",
    countQuery = "select count(*)  from mrp2_supplier_arrival_plan a " +
            "where a.date>=:startDate and a.date <:endDate " +
            "and a.material like :material and a.supplier like :supplier and a.plant like :plant " +
            "GROUP BY a.supplier, a.material, a.plant", nativeQuery = true)
    Page<Map> pageQuerySupplierArrivalPlan(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                           @Param("plant") String plant,
                                           @Param("material") String material, @Param("supplier") String supplier, Pageable pageable);


    @Query(value = "select a.date, SUM(arrival_qty) as arrivalQty " +
            "from mrp2_supplier_arrival_plan a " +
            "where a.date>=:startDate and a.date <:endDate and a.material=:material and a.supplier=:supplierCode and a.plant =:plant " +
            "GROUP BY a.plant, a.supplier, a.material, a.date",  nativeQuery = true)
    List<Map> getSupplierArrivalQty(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                    @Param("plant") String plant, @Param("material") String material,
                                    @Param("supplierCode") String supplierCode);

    @Query(value = "select date as fabDate, sum(arrival_qty) as qty from mrp2_supplier_arrival_plan where material=:material and plant=:plant " +
            "and date>=:startDate and date<=:endDate GROUP BY plant,material,date", nativeQuery = true)
    List<Map> getDaySupplierArrivalQty(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                       @Param("plant") String plant, @Param("material") String material);

    /**
     * 查询缺料分配和供应商到货计划
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param plant 厂别
     * @param material 料号
     * @param supplier 供应商
     * @return Page<Map>
     */
    @Query(value = "select tt.*,m.material_name as materialName,m.material_group as materialGroup,s.name as supplierName    " +
            "            from (    " +
            "                select material,plant,fabDate,supplier,allocationQty,arrivalQty from (    " +
            "                SELECT a.material,  a.plant, a.fab_date as fabDate, a.supplier, a.allocation_qty as allocationQty, p.arrival_qty as arrivalQty    " +
            "                FROM mrp2_allocation a    " +
            "                LEFT JOIN mrp2_supplier_arrival_plan p ON a.material=p.material and a.plant=p.plant and a.fab_date=p.date and a.supplier=p.supplier   " +
            "                where a.fab_date>=:startDate and a.fab_date<=:endDate and a.plant like :plant and a.material like :material and a.supplier like :supplier  " +
            "                UNION    " +
            "                SELECT p.material, p.plant, p.date as fabDate, p.supplier, a.allocation_qty as allocationQty, p.arrival_qty as arrivalQty    " +
            "                FROM mrp2_supplier_arrival_plan P    " +
            "                LEFT JOIN mrp2_allocation a ON a.material=p.material and a.plant=p.plant and a.fab_date=p.date and a.supplier=p.supplier    " +
            "                where p.date>=:startDate and date<=:endDate and p.plant like :plant and p.material like :material and p.supplier like :supplier  " +
            "                ) t    " +
            "                order by t.material, t.supplier   " +
            "            ) tt    " +
            "            LEFT JOIN mrp2_material m on m.material=tt.material    " +
            "            LEFT JOIN mrp2_supplier s on s.id=tt.supplier", nativeQuery = true)
    List<Map> getAllocationArrivalPlan(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
                                       @Param("plant") String plant,
                                       @Param("material") String material, @Param("supplier") String supplier);
}
