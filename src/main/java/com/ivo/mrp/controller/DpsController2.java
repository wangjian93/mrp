//package com.ivo.mrp.controller;
//
//import com.ivo.common.result.PageResult;
//import com.ivo.common.utils.ResultUtil;
//import com.ivo.mrp.service.DpsService2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * DPS管理
// * @author wj
// * @version 1.0
// */
//@RestController
//@RequestMapping("/mrp")
//public class DpsController2 {
//
//    private DpsService2 dpsService2;
//
//    @Autowired
//    public DpsController2(DpsService2 dpsService2) {
//        this.dpsService2 = dpsService2;
//    }
//
//    /**
//     * 动态查询条件，根据月份、供应商、料号查询到料计划
//     * @param month    月份
//     * @param model   供应商
//     * @param product 料号
//     * @return PageResult
//     */
//    @RequestMapping("/dps")
//    public PageResult getDps(String month, String model, String product) {
//        return ResultUtil.successPage(dpsService2.getDps(month, model, product));
//    }
//
//}
