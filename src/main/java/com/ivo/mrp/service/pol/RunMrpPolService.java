package com.ivo.mrp.service.pol;

import com.ivo.mrp.entity.MrpVer;

/**
 * @author wj
 * @version 1.0
 */
public interface RunMrpPolService {

    void runMrpPol(String[] dpsVers, String user);

    MrpVer createMrpVer(String[] dpsVers, String user);

    void computeDemandQty(String ver);

    void computeDemandQty(String ver, String dpsVer);

    void computeDemandQty(String ver, String dpsVer, String product);


    void computeBalanceQty(String ver);

    void computeBalanceQty(String ver, String product, String material);
}
