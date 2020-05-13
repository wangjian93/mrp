package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MaterialDailyBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MaterialDailyBalanceRepository extends JpaRepository<MaterialDailyBalance, Long> {

    List<MaterialDailyBalance> findByMrpVer(String mrpVer);

    List<MaterialDailyBalance> findByMrpVerAndMaterial(String mrpVer, String material);
}
