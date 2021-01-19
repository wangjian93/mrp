package com.ivo.test.service;

import com.ivo.mrp.service.ActualArrivalService;
import com.ivo.test.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author wj
 * @version 1.0
 */
public class ActualArrivalServiceTest extends AbstractTest {

    @Autowired
    private ActualArrivalService actualArrivalService;

    @Test
    public void test_syncActualArrival() {
//        String[] sts = new String[] {"2021-01-04","2021-01-05","2021-01-06"};
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        for(String s : sts) {
//            Date fabDate = null;
//            try {
//                fabDate = new Date(sdf.parse(s).getTime());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            actualArrivalService.syncActualArrival(fabDate);
//        }
        actualArrivalService.syncActualArrival();
    }
}
