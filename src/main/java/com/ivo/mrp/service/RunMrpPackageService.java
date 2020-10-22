package com.ivo.mrp.service;

/**
 * @author wj
 * @version 1.0
 */
public interface RunMrpPackageService {

    /**
     * run mrp
     * @param dpsVers dps
     * @param mpsVers mps
     * @param user 用户
     */
    void runMrp(String[] dpsVers, String[] mpsVers, String user);

    /**
     * 5 计算MRP 包材
     * @param ver MRP版本
     */
    void computeMrpPackage(String ver);

    void computeMrpPackage(String ver, String dpsVer);

    void computeMrpPackage(String ver, String dpsVer, String product, String type, Double linkQty, String mode);



    void computeDemand(String ver);

    void computeDpsDemand(String ver, String dpsVer);

    void computeDpsDemand(String ver, String dpsVer, String product, String type, Double linkQty, String mode);

    void computeMrpMaterial(String ver);

    void computeMrpBalance(String ver);

    void computeMrpBalance(String ver, String product, String type, Double linkQty, String mode, String material);
}
