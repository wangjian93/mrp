//package com.ivo.mrp.controller;
//
//import com.ivo.common.result.PageResult;
//import com.ivo.common.utils.ResultUtil;
//import com.ivo.mrp.service.MaterialScrapRateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 物料损耗率
// * @author wj
// * @version 1.0
// */
//@RestController
//@RequestMapping("/mrp/materialScrapRat")
//public class MaterialScrapRatController {
//
//    private MaterialScrapRateService materialScrapRateService;
//
//    @Autowired
//    public MaterialScrapRatController(MaterialScrapRateService materialScrapRateService) {
//        this.materialScrapRateService = materialScrapRateService;
//    }
//
//    /**
//     * 获取物料损耗率列表
//     * @return Result
//     */
//    @GetMapping("/list")
//    public PageResult list() {
//        return ResultUtil.successPage(materialScrapRateService.getMaterialScrapRate());
//    }
//}
