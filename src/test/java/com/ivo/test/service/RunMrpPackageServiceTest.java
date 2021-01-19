package com.ivo.test.service;

import com.ivo.mrp.service.packageing.RunMrpPackageService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class RunMrpPackageServiceTest extends AbstractTest {

    @Autowired
    private RunMrpPackageService runMrpPackageService;

    @Test
    public void test_runMrp() {
        String[] dpsVers = new String[] {"20210106111"};
        runMrpPackageService.runMrp(dpsVers, "SYS");
    }

}
