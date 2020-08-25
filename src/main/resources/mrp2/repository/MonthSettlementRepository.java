package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MonthSettlement;
import com.ivo.mrp2.key.MonthSettlementKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MonthSettlementRepository extends JpaRepository<MonthSettlement, MonthSettlementKey> {

    List<MonthSettlement> findByPlantAndMonth(String plant, String month);
}
