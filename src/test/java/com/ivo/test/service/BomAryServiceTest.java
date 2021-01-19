package com.ivo.test.service;

import com.ivo.mrp.service.ary.BomAryService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class BomAryServiceTest extends AbstractTest {

    @Autowired
    private BomAryService bomAryService;

    @Test
    public void test_syncBomAry() {
        bomAryService.syncAryMpsMode();
        bomAryService.syncBomAryProduct();
        bomAryService.syncBomAry();
    }
}
