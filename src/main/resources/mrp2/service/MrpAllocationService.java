package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpAllocation;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpAllocationService {

    List<MrpAllocation> list();

    /**
     * 获取材料某日的分配详细
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return  List<MrpAllocation>
     */
    List<MrpAllocation> getMrpAllocation(String plant, String material, Date fabDate);

    /**
     * 获取MrpAllocation
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @param supplier 厂商
     * @return MrpAllocation
     */
    MrpAllocation getMrpAllocation(String plant, String material, Date fabDate, String supplier);


    /**
     * 获取材料某日分配的数量
     * @param plant 厂别
     * @param material 料号
     * @param fabDate 日期
     * @return double
     */
    double getAllocationQry(String plant, String material, Date fabDate);

    /**
     * 保存
     * @param mrpAllocationList List<MrpAllocation>
     */
    void saveMrpAllocation(List<MrpAllocation> mrpAllocationList);

}
