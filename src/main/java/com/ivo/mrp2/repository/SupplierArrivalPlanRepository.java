package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.SupplierArrivalPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface SupplierArrivalPlanRepository extends JpaRepository<SupplierArrivalPlan, Long> {

    @Query(value = "select sum(plan_qty) from mrp2_supplier_arrival_plan where material=:material and date=:date", nativeQuery = true)
    Double getMaterialArrivalPlanQty(String material, Date date);

    @Query("from mrp2_supplier_arrival_plan t where t.material=:material and t.date>=:fromDate and t.date<=:toDate")
    List<SupplierArrivalPlan> getSupplierArrivalPlan(Date fromDate, Date toDate, String material);

    @Query("from mrp2_supplier_arrival_plan t where t.date>=:fromDate and t.date<=:toDate")
    List<SupplierArrivalPlan> getSupplierArrivalPlan(Date fromDate, Date toDate);
}
