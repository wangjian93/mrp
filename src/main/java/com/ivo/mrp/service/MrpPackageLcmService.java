package com.ivo.mrp.service;

import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcm;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageLcmService {

    void saveMrpPackageLcm(List<MrpPackageLcm> list);

    void saveMrpPackageLcmMaterial(List<MrpPackageLcmMaterial> list);

    List<MrpPackageLcmMaterial> getMrpPackageLcmAloneMaterial(String ver);

    List<MrpPackageLcmMaterial> getMrpPackageLcmProductMaterial(String ver);

    List<MrpPackageLcmMaterial> getMrpPackageLcmMaterial(String ver);

    List<MrpPackageLcm> getMrpPackageLcmAlone(String ver, String material);

    List<MrpPackageLcm> getMrpPackageLcmProduct(String ver, String product, String material);

    Page<MrpPackageLcmMaterial> getPageMrpPackageLcmMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup,
                                                             String searchMaterial, String searchSupplier);

}
