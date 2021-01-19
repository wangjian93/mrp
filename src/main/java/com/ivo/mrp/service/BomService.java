package com.ivo.mrp.service;

import com.ivo.mrp.entity.direct.lcm.BomLcm;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * BOM的服务接口
 * @author wj
 * @version 1.0
 */
public interface BomService {

    /**
     * 同步LCM的BOM
     */
    void syncBomLcm();

    /**
     * 获取LCM的机种BOM
     * @param product 机种
     * @Param fab 厂别
     * @return List<BomLcm>
     */
    List<BomLcm> getLcmBom(String product, String fab);

    /**
     * 主材查询BOM的机种
     * @param page 页数
     * @param limit 分页大小
     * @param fab 厂别
     * @param searchProduct 机种查询
     * @return Page<Map>
     */
    Page<Map> queryProduct(int page, int limit, String fab, String searchProduct);
}
