//package com.ivo.mrp.service.impl;
//
//import com.ivo.mrp.entity.Dps2;
//import com.ivo.mrp.repository.DpsRepository2;
//import com.ivo.mrp.service.DpsService2;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Example;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
///**
// * @author wj
// * @version 1.0
// */
//@Service
//public class DpsService2Impl implements DpsService2 {
//
//    private DpsRepository2 repository;
//
//    @Autowired
//    public DpsService2Impl(DpsRepository2 repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public List<Dps2> getDps(String month, String model, String product) {
//        Dps2 dps2 = new Dps2();
//        if(StringUtils.isNoneEmpty(month)) {
//            dps2.setMonth(month);
//        }
//        if(StringUtils.isNoneEmpty(model)) {
//            dps2.setModel(model);
//        }
//        if(StringUtils.isNoneEmpty(product)) {
//            dps2.setProduct(product);
//        }
//
//        Example<Dps2> example = Example.of(dps2);
//        return repository.findAll(example);
//    }
//}
