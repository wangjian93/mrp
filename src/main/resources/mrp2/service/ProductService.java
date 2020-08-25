package com.ivo.mrp2.service;

import java.util.List;

/**
 * 机种服务
 * @author wj
 * @version 1.0
 */
public interface ProductService {

    /**
     * 同步机种
     */
    void syncProduct();

    /**
     * 查询机种
     * @param productSearch 机种模糊查询
     * @param limit 最大条数
     * @return List<String>
     */
    List<String> searchProduct(String productSearch, int limit);
}
