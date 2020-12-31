package com.ivo.mrp.service;

import com.ivo.mrp.entity.lcmPackaging.AloneMaterial;
import com.ivo.mrp.entity.lcmPackaging.BomPackagingLcm;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface BomPackageLcmService {

    /**
     * LCM包材查询BOM的机种
     * @param page 页数
     * @param limit 分页大小
     * @param fab 厂别
     * @param searchProduct 机种查询
     * @return Page<Map>
     */
    Page<Map> queryProduct(int page, int limit, String fab, String searchProduct);

    /**
     * 获取LCM包材的机种BOM
     * @param product 机种
     * @return List<Map>
     */
    List<BomPackagingLcm> getLcmPackageBom(String fab, String product);


    /**
     * 同步Lcm包材BOM
     */
    void syncLcmPackageBom();


    List<AloneMaterial> getAloneMaterial();

    void syncAloneMaterial();
}
