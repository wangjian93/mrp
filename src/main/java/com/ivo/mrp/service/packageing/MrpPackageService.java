package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.entity.packaging.MrpPackageMaterial;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageService {

    void saveMrpPackageMaterial(List<MrpPackageMaterial> list);

    void saveMrpPackage(List<MrpPackage> list);

    Page<MrpPackageMaterial> getPageMrpPackageMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup,
                                                       String searchMaterial, String searchSupplier);

    List<MrpPackage> getMrpPackage(String ver, String packageId, String material);
}
