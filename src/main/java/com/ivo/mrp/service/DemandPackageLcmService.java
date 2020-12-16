package com.ivo.mrp.service;

import com.ivo.mrp.entity.lcmPackaging.DemandPackageLcm;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DemandPackageLcmService {

    void saveDemandPackageLcm(List<DemandPackageLcm> list);

    List<String> getDemandAloneMaterial(String ver);

    List<Map> getDemandProductMaterial(String ver);

    Map<Date, Double> getDemandQtyAlone(String ver, String material);

    Map<Date, Double> getDemandQtyProduct(String ver, String product, String material);


    List<String> getDemandAloneMaterialProduct(String ver, String material);

    List<Map> getDemandQtyAloneDetail(String ver, String material);
}
