package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.MrpVer;

/**
 * @author wj
 * @version 1.0
 */
public interface RunMrpPackageService {

    /**
     * run mrp
     * @param dpsVers dps
     * @param user 用户
     */
    void runMrp(String[] dpsVers, String user);

    MrpVer createMrpVer(String[] dpsVers, String user);

    /**
     * 计算MRP 包材
     * @param ver MRP版本
     */
    void computeMrpPackage(String ver);
}
