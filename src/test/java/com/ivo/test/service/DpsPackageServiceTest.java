package com.ivo.test.service;

import com.ivo.mrp.service.packageing.DpsPackageService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class DpsPackageServiceTest extends AbstractTest {

    @Autowired
    private DpsPackageService dpsPackageService;

    @Test
    public void test_syncDpsPackage() {
        dpsPackageService.syncDpsPackage("20201214093005");
    }
}
