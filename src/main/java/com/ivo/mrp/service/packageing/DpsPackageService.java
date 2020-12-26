package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.DpsCellProduct;
import com.ivo.mrp.entity.packaging.DpsPackage;
import org.springframework.data.domain.Page;

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

    List<DpsPackage> getDpsPackage(String dpsVer, String packageId);

    Page<Map> getPagePackageId(String ver, int page, int limit, String searchProduct);

    List<DpsCellProduct> getDpsCellProduct(String ver, String product);

    List<DpsPackage> getDpsPackageByProduct(String ver, String product);

    void delete(List<DpsPackage> dpsPackageList);

    void save(List<DpsPackage> dpsPackageList);


}
