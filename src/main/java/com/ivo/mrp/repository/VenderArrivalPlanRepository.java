package com.ivo.mrp.repository;

import com.ivo.mrp.entity.VenderArrivalPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface VenderArrivalPlanRepository extends JpaRepository<VenderArrivalPlan, Long> {

    List<VenderArrivalPlan> findByMonth(String month);

}
