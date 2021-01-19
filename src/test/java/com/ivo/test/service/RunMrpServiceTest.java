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
        String[] products = new String[] {"N1405 R0/R2/R3/R7/R7-P/R8/RB/RB-P/RD/RF"};
        String mrpVer = "20201214054";
        String dpsVer = "20201214089";
        for(String product : products) {
            runMrpService.computeDpsDemandCell(mrpVer, dpsVer, product);
        }
    }

    @Test
    public void test_() {
        runMrpService.updateMrp("20210107090");
    }

}


