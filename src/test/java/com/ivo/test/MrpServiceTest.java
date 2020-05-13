package com.ivo.test;

import com.ivo.mrp2.service.MrpService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MrpServiceTest extends AbstractTest {

    @Autowired
    private MrpService mrpService;

    @Test
    public void t1_generateMrp() {
        mrpService.generateMrp("MC1589275326215LCM1");
    }

    @Test
    public void t2_determineDateRange() {
        mrpService.determineDateRange("MC1589275326215LCM1_0");
    }

    @Test
    public void t3_dpsExpandBom() {
        mrpService.expandDps("MC1589275326215LCM1_0");
    }

    @Test
    public void  computeDemand() {
        mrpService.computeDemand("MC1589275326215LCM1_0");
    }

    @Test
    public void computeLoss() {
        mrpService.computeLoss("MC1589275326215LCM1_0");
    }


//
//    @Test
//    public void t4_computeLoss() {
//        mrpService.computeLoss("MC1589184921700LCM1_0");
//        mrpService.computeLoss("MC1589182963576CELL_1");
//    }
//
//    @Test
//    public void t5_computeBalance() {
//        mrpService.computeBalance("MC1589184921700LCM1_0");
//        mrpService.computeBalance("MC1589182963576CELL_1");
//    }
}
