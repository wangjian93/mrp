package com.ivo.test.service;

import com.ivo.mrp.service.pol.DpsPolService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author wj
 * @version 1.0
 */
public class DpsPolServiceTest extends AbstractTest {

    @Autowired
    private DpsPolService dpsPolService;

    @Test
    public void test_syncDpsPol() {
        dpsPolService.syncDpsPol("20201214093005");
    }
}
