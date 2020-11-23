package com.ivo.test.service;

import com.ivo.rest.RestService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public class RestServiceTest extends AbstractTest {

    @Autowired
    private RestService restService;

    @Test
    public void test_getSupplierMaterial() {
        List list  = restService.getSupplierMaterial();
        list.size();
    }

    @Test
    public void test_getActualArrivalQty() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fabDate = new Date(sdf.parse("2020-10-27").getTime());
        List list  = restService.getActualArrivalQty(fabDate);
        list.size();
    }

    @Test
    public void test() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        double d = restService.getGoodInventory("ARY", "5100014-00", new Date(sdf.parse("2020-10-01").getTime()));
        System.out.println(d);
    }
}
