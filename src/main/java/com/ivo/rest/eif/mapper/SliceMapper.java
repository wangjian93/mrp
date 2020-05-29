package com.ivo.rest.eif.mapper;

import com.ivo.mrp2.entity.ProductSlice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 切片数
 * @author wj
 * @version 1.0
 */
@Repository
public interface SliceMapper {

    /**
     * 获取机种的切片数
     * @return  List<ProductSlice>
     */
    List<ProductSlice> getProductSlice();
}
