package com.ivo.test;

import com.ivo.mrp2.service.ProductSliceService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class ProductSliceServiceTest extends AbstractTest {

    @Autowired
    private ProductSliceService productSliceService;

    @Test
    public void test() {
        productSliceService.syncProductSlice();
    }
}
