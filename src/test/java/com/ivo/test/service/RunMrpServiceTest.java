package com.ivo.test.service;

import com.ivo.mrp.service.RunMrpService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class RunMrpServiceTest extends AbstractTest {

    @Autowired
    private RunMrpService runMrpService;

    @Test
    public void test_createMrpVer() {
        String[] dpsVers = new String[] {"20200902030"};
        String[] mpsVers = new String[] {};
        runMrpService.createMrpVer(dpsVers, mpsVers, "SYS");
    }

    @Test
    public void test_computeDpsDemand() {
        runMrpService.computeDpsDemand("20200909003", "20200902030");
    }

    @Test
    public void test_computeDemand() {
        runMrpService.computeDemand("20200909001");
    }

    @Test
    public void test_computeMrpMaterial() {
        runMrpService.computeMrpMaterial("20200909001");
    }

    @Test
    public void test_computeMrpBalance() {
        runMrpService.computeMrpBalance("20200909003");
    }

    @Test
    public void test_runMrp() {
        String[] dpsVers = new String[] {"20200902031"};
        String[] mpsVers = new String[] {};
        runMrpService.runMrp(dpsVers, mpsVers, "SYS");
    }

    @Test
    public void test_completeMrpMaterial() {
        runMrpService.completeMrpMaterial("20201113025");

//        String[] products = new String[] {"N1012 R0/R1/R2", "N140CV 0.15t连R0_EQ", "S0655 R0"};
//        String mrpVer = "20201105019";
//        String dpsVer = "20201103040";
//        String mpsVer = "20201105009";
//        for(String product : products) {
//            runMrpService.computeDpsDemandAry(mrpVer, dpsVer, product);
//            runMrpService.computeMpsDemandCell(mrpVer, mpsVer, product);
//        }

    }

    @Test
    public void test_() {
        runMrpService.completeMrpMaterial("20201210049");
    }


}


