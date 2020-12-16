package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.DpsPackage;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsPackageService {

    /**
     * 同步包材DPS
     */
    void syncDpsPackage();
    void syncDpsPackage(String ver);

    List<String> getPackageId(List<String> dpsVers);

    List<DpsPackage> getDpsPackage(List<String> dpsVers, String packageId);
}
