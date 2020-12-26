package com.ivo.mrp.service.pol;

import com.ivo.mrp.entity.pol.MrpPol;
import com.ivo.mrp.entity.pol.MrpPolMaterial;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPolService {

    void saveMrpPol(List<MrpPol> list);

    void saveMrpPolMaterial(List<MrpPolMaterial> list);

    List<MrpPolMaterial> getMrpPolMaterial(String ver);

    List<MrpPol> getMrpPol(String ver, String product, String material);

    MrpPolMaterial getMrpPolMaterial(String ver, String product, String material);

    Page<MrpPolMaterial> getPageMrpPol(int page, int limit,String ver, String searchProduct,
                                       String searchMaterialGroup, String searchMaterial, String searchSupplier);
}
