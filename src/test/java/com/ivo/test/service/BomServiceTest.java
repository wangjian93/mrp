package com.ivo.test.service;

import com.ivo.mrp.service.BomService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * bom数据同步测试
 * @author wj
 * @version 1.0
 */
public class BomServiceTest extends AbstractTest {

    @Autowired
    private BomService bomService;


    @Test
    public void test_syncBomLcm() {
        bomService.syncBomLcm();
    }
}
