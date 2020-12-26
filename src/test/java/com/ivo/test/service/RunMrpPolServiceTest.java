package com.ivo.test.service;

import com.ivo.mrp.service.pol.RunMrpPolService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
public class RunMrpPolServiceTest extends AbstractTest {

    @Autowired
    private RunMrpPolService runMrpPolService;

    @Test
    public void test_runMrpPol() {
        String[] dpsVers = new String[]{"20201218094"};
        runMrpPolService.runMrpPol(dpsVers, "SYS");
    }
}
