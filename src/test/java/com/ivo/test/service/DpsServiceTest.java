package com.ivo.test.service;

import com.ivo.mrp.service.DpsService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DPS数据同步测试
 * @author wj
 * @version 1.0
 */
public class DpsServiceTest extends AbstractTest {

    @Autowired
    private DpsService dpsService;

    @Test
    public void test_syncDpsLcm() {
        dpsService.syncDpsLcm();
    }

    @Test
    public void test_syncDpsCell() {
        dpsService.syncDpsCell();
    }

    @Test
    public void test_syncDpsAry() {
        dpsService.syncDpsAry();
    }

    @Test
    public void test_syncDpsPackage() {
        dpsService.syncDpsPackage();
    }
}
