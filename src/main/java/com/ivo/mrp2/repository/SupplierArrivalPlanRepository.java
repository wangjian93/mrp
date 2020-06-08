package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.SupplierArrivalPlan;
import com.ivo.mrp2.key.SupplierArrivalPlanKey;
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
public interface SupplierArrivalPlanRepository extends JpaRepository<SupplierArrivalPlan, SupplierArrivalPlanKey> {

    @Query(value = "select sum(arrival_qty) from mrp_supplier_arrival_plan where plant=:plant and material=:material and date=:date", nativeQuery = true)
    Double getMaterialArrivalPlanQty(String plant, String material, Date date);

    List<SupplierArrivalPlan> findByPlantAndMaterialAndDate(String plant, String material, Date fabDate);

    @Query(value = "select a.plant, a.supplier_code as supplierCode, a.material  from mrp_supplier_arrival_plan a " +
            "where a.date>=:startDate and a.date <:endDate " +
            "and a.material like :material and a.supplier_code like :supplier and a.plant like :plant " +
            "GROUP BY a.supplier_code, a.material, a.plant",
    countQuery = "select count(*)  from mrp_supplier_arrival_plan a " +
            "where a.date>=:startDate and a.date <:endDate " +
            "and a.material like :material and a.supplier_code like :supplier and a.plant like :plant " +
            "GROUP BY a.supplier_code, a.material, a.plant", nativeQuery = true)
    Page<Map> pageQuerySupplierArrivalPlan(Date startDate, Date endDate, String plant,
                                           String material, String supplier, Pageable pageable);


    @Query(value = "select a.date, SUM(arrival_qty) as arrivalQty " +
            "from mrp_supplier_arrival_plan a " +
            "where a.date>=:startDate and a.date <:endDate and a.material=:material and a.supplier_code=:supplierCode and a.plant =:plant " +
            "GROUP BY a.plant, a.supplier_code, a.material, a.date",  nativeQuery = true)
    List<Map> getSupplierArrivalQty(Date startDate, Date endDate, String plant, String material, String supplierCode);

    @Query(value = "select date as fabDate, sum(arrival_qty) as qty from mrp_supplier_arrival_plan where material=:material and plant=:plant " +
            "and date>=:startDate and date<=:endDate GROUP BY plant,material,date", nativeQuery = true)
    List<Map> getDaySupplierArrivalQty(Date startDate, Date endDate, String plant, String material);
}
