package com.ivo.test;

import com.ivo.mrp2.service.MrpService2;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wj
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MrpService2Test extends AbstractTest {

    @Autowired
    private MrpService2 mrpService;

    private static String dpsVer = "20200529MC25";
    private static String mrpVer = "20200529MC25-0";


//    @Test
//    public void t1_generateMrpVer() {
//        mrpVer = mrpService.generateMrpVer(dpsVer);
//    }
//
//    @Test
//    public void t2_computeDemand() {
//        mrpService.computeDemand(mrpVer);
//    }

    @Test
    public void t3_generateMrpData() {
        mrpService.generateMrpData(mrpVer);
    }

    @Test
    public void t4_computeLossQty() {
        mrpService.computeLossQty(mrpVer);
    }

    @Test
    public void t5_computeArrivalQty() {
        mrpService.computeArrivalQty(mrpVer);
    }

    @Test
    public void t6_computeBalance() {
        mrpService.computeBalance(mrpVer);
    }
}
