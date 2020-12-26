package com.ivo.mrp.service;

import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcm;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
public interface RunMrpPackageLcmService {

    MrpVer createMrpVer(String[] dpsVers, String[] mpsVers, String user);

    void computeDemand(String ver);


    void computeDpsDemand(String ver, String dpsVer);

    void computeDpsDemand(String ver,  String dpsVer, String product);



    void computeMpsDemand(String ver, String mpsVer);

    void computeMpsDemand(String ver,  String mpsVer, String product);


    void computeMrpMaterial(String ver);

    void computeMrpBalance(String ver);

    void computeMrpBalanceAloneMaterial(String ver);

    void computeMrpBalanceAloneMaterial(String ver, MrpPackageLcmMaterial mrpPackageLcmMaterial);

    void computeMrpBalanceProductMaterial(String ver);

    void computeMrpBalanceProductMaterial(String ver, MrpPackageLcmMaterial mrpPackageLcmMaterial);

    void completeMrpMaterial(String ver);


    void runMrp(String[] dpsVers, String[] mpsVers, String user);


    void updateMrpPackageMaterial(String ver, String product, String material);

    void updateMrpBalanceQty(String ver, String product, String material, Date fabDate, double balanceQty);
}
