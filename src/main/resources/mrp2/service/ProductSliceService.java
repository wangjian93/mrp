package com.ivo.mrp2.service;

/**
 * 机种切片数获取服务接口
 * @author wj
 * @version 1.0
 */
public interface ProductSliceService {

    /**
     * 获取机种的切片数
     * @param product 机种
     * @return double
     */
   double getProductSliceService(String product);

    /**
     * 同步机种的切片数
     */
   void syncProductSlice();
}
