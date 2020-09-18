package com.ivo.test.service;

import com.ivo.mrp.entity.MonthSettle;
import com.ivo.mrp.service.MonthSettleService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;

/**
 * 需求月结功能测试
 * @author wj
 * @version 1.0
 */
public class MonthSettleServiceTest extends AbstractTest {

    @Autowired
    private MonthSettleService monthSettleService;

    @Test
    public void test() {
        java.sql.Date settleDate = new java.sql.Date(new Date().getTime());
        monthSettleService.addMonthSettle("CELL", "A1022", "103", "2020-07", settleDate, 100D, "SYS");
        List<MonthSettle> list =  monthSettleService.getMonthSettle("CELL", "2020-07");
        Assert.notEmpty(list, "测试失败");
    }
}
