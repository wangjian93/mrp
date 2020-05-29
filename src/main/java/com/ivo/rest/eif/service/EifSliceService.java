package com.ivo.rest.eif.service;

import com.ivo.mrp2.entity.ProductSlice;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface EifSliceService {

    /**
     * 获取机种的切片数
     * @return List<ProductSlice>
     */
    List<ProductSlice> getProductSlice();
}
