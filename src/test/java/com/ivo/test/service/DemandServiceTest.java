package com.ivo.test.service;

import com.ivo.mrp.service.DemandService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public class DemandServiceTest extends AbstractTest {

    @Autowired
    private DemandService demandService;

    @Test
    public void test() {
        Map list = demandService.getDemandQtyCell("20201105019", "1606019-00");
        list.size();
    }
}
