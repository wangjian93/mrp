package com.ivo.mrp.service.pol;

import com.ivo.mrp.entity.pol.DpsCellPolProduct;
import com.ivo.mrp.entity.pol.DpsPol;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface DpsPolService {

    void syncDpsPol();
    void syncDpsPol(String ver);

    Page<Map> getPageProduct(String ver, int page, int limit, String searchProduct);

    List<DpsPol> getDpsPol(String ver, String product);

    List<DpsCellPolProduct> getDpsCellPolProduct(String ver, String project);

    void delete(List<DpsPol> list);

    void save(List<DpsPol> list);

    List<DpsPol> getDpsPolByProject(String ver, String project);

    List<String> getDpsPolProduct(String ver);
}
