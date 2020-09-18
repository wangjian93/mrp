package com.ivo.mrp.repository;

import com.ivo.mrp.entity.MonthSettle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MonthSettleRepository extends JpaRepository<MonthSettle, Long> {

    /**
     * 筛选厂别、机种、物料组、结算月份
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param month 结算月份
     * @param validFlag 有效性
     * @return List<MonthSettle>
     */
    MonthSettle findFirstByFabAndProductAndMaterialGroupAndMonthAndValidFlag(String fab, String product,
                                                                              String materialGroup, String month, boolean validFlag);


    /**
     * 筛选厂别、月份
     * @param fab 厂别
     * @param month 结算月份
     * @param validFlag 有效性
     * @return List<MonthSettle>
     */
    List<MonthSettle> findByFabAndMonthAndValidFlag(String fab, String month, boolean validFlag);
}
