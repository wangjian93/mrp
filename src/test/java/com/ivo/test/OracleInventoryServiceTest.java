package com.ivo.test;

import com.ivo.rest.oracle.service.OracleInventoryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author wj
 * @version 1.0
 */
public class OracleInventoryServiceTest extends AbstractTest {

    @Autowired
    private OracleInventoryService oracleInventoryService;

    @Test
    public void test() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(sdf.parse("2020-05-01").getTime());
        double d = oracleInventoryService.getGoodInventory("LCM1", "29209L1-000", date);
        double d2 = oracleInventoryService.getDullInventory("LCM1", "29209L1-000", date);
        System.out.println(d);
        System.out.println(d2);
    }
}
