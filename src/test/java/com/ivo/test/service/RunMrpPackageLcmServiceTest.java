package com.ivo.test.service;

import com.ivo.mrp.service.RunMrpPackageLcmService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class RunMrpPackageLcmServiceTest extends AbstractTest {

    @Autowired
    private RunMrpPackageLcmService runMrpPackageLcmService;

    @Test
    public void test_runMrp() {
//        String[] dpsVers = new String[] {"20201207073","20201207074"};
//        String[] mpsVers = new String[] {"20201209014","20201209016"};
//        runMrpPackageLcmService.runMrp(dpsVers, mpsVers, "SYS");

        runMrpPackageLcmService.completeMrpMaterial("20201218067");
    }
}
