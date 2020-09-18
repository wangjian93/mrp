package com.ivo.test.service;

import com.ivo.mrp.service.LossRateService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 损耗率测试
 * @author wj
 * @version 1.0
 */
public class LossRateServiceTest extends AbstractTest {

    @Autowired
    private LossRateService lossRateService;

    @Test
    public void test_MaterialLossRate() {
        lossRateService.saveMaterialLossRate("240SF02-000", 45, "TEST");
        Double lossRate = lossRateService.getLossRate("240SF02-000");
        Assert.notNull(lossRate, "测试失败");
    }

    @Test
    public void test_MaterialGroupLossRate() {
        lossRateService.saveMaterialGroupLossRate("913", 2, "TEST");
        lossRateService.saveMaterialLossRate("240SF02-000", 45, "TEST");
        Double lossRate = lossRateService.getLossRate("240SF01-000");
        Assert.notNull(lossRate, "测试失败");
    }
}
