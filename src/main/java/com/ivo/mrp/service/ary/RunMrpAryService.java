package com.ivo.mrp.service.ary;

import com.ivo.mrp.entity.MrpVer;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
public interface RunMrpAryService {

    /**
     * 1 创建MRP版本
     * @param dpsVers DPS版本
     * @param mpsVers MPS版本
     * @param user 用户
     * @return MrpVer
     */
    MrpVer createMrpVer(String[] dpsVers, String[] mpsVers, String user);

    /**
     * 1_1 DPS与MPS的版本检查
     * @param dpsVers DPS版本
     * @param mpsVers MPS版本
     * @return MrpVer
     */
    MrpVer checkDpsAndMps(String[] dpsVers, String[] mpsVers);


    /**
     * 1_2 生成MRP的版本
     * @return String
     */
    String generateMpsVer();

    /**
     * 2 计算需求量
     * @param ver MRP版本
     */
    void computeDemand(String ver);

    /**
     * 2_1 计算DPS的需求量
     * @param ver MRP版本
     * @param dpsVer dps版本
     */
    void computeDpsDemand(String ver, String dpsVer);

    void computeDpsDemand(String ver,  String dpsVer, String product);

    /**
     * 2_1_2 ARY
     */
    void computeDpsDemandAry(String ver, String dpsVer);

    void computeDpsDemandAry(String ver,  String dpsVer, String product);

    void computeDpsDemandAryOc(String ver,  String dpsVer, String product);

    /**
     * 2_2 计算MPS的需求量
     * @param ver MRP版本
     * @param mpsVer mps版本
     */
    void computeMpsDemand(String ver, String mpsVer);

    void computeMpsDemand(String ver,  String mpsVer, String product);

    /**
     * 2_2_2 ARY
     */
    void computeMpsDemandAry(String ver, String mpsVer);

    void computeMpsDemandAry(String ver,  String mpsVer, String product);

    /**
     * 2_3 计算月结的需求量
     * @param ver MRP版本
     */
    void computeSettleDemand(String ver);

    /**
     * 2_3_2 ARY
     */
    void computeSettleDemandAry(String ver, String fab, String month);

    /**
     * 3 计算MRP材料的期初库存、损耗率
     * @param ver MRP版本
     */
    void computeMrpMaterial(String ver);

    void computeMrpMaterialAry(String ver);


    /**
     * 4 计算MRP结余量
     * @param ver MRP版本
     */
    void computeMrpBalance(String ver);

    void computeMrpBalanceAry(String ver);

    void computeMrpBalanceAry(String ver, String material);

    void computeMrpBalance(String ver, String material);


    /**
     * run mrp
     * @param dpsVers dps
     * @param mpsVers mps
     * @param user 用户
     */
    void runMrp(String[] dpsVers, String[] mpsVers, String user);


    /**
     * 完善MRP的信息
     * @param ver MRP版本
     */
    void completeMrpMaterial(String ver);

    /**
     * 更新料号的MRP
     * @param ver MRP版本
     * @param material 料号
     */
    void updateMrpMaterial(String ver, String material);

    /**
     * 修改MRP料号的结余量
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @param balanceQty 结余量
     */
    void updateMrpBalanceQty(String ver, String material, Date fabDate, double balanceQty);

    /**
     * 更新MRP的供应商分配数量
     * @param ver MRP版本
     * @param material 料号
     * @param fabDate 日期
     */
    double updateMrpAllocationQty(String ver, String material, Date fabDate);


    void updateMrp(String ver);
}
