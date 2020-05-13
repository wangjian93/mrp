package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MaterialDailyBalance;

import java.util.List;

/**
 * 计算材料每日结余量服务接口
 * @author wj
 * @version 1.0
 */
public interface MaterialDailyBalanceService {

    /**
     * 批量保存
     * @param list List<MaterialDailyBalance>
     */
    void batchSave(List<MaterialDailyBalance> list);

    /**
     * 获取获取材料结余
     * @param mrpVer mrp版本
     * @return
     */
    List<MaterialDailyBalance> getMaterialDailyBalance(String mrpVer);

    /**
     * 获取获取材料结余
     * @param mrpVer mrp版本
     * @param material 料号
     * @return
     */
    List<MaterialDailyBalance> getMaterialDailyBalance(String mrpVer, String material);
}
