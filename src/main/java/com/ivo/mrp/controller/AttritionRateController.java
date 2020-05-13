//package com.ivo.mrp.controller;
//
//import com.ivo.common.result.PageResult;
//import com.ivo.common.result.Result;
//import com.ivo.common.utils.ResultUtil;
//import com.ivo.mrp.entity.AttritionRate;
//import com.ivo.mrp.service.AttritionRateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.propertyeditors.CustomDateEditor;
//import org.springframework.web.bind.ServletRequestDataBinder;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * @author wj
// * @version 1.0
// */
//@RestController
//@RequestMapping("/mrp")
//public class AttritionRateController {
//
//    private AttritionRateService attritionRateService;
//
//    @Autowired
//    public AttritionRateController(AttritionRateService attritionRateService) {
//        this.attritionRateService = attritionRateService;
//    }
//
//    @InitBinder
//    public void initBinder(ServletRequestDataBinder bin) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        CustomDateEditor customDateEditor = new CustomDateEditor(sdf, true);
//        bin.registerCustomEditor(Date.class, customDateEditor);
//    }
//
//    /**
//     * 保存或修改损耗率
//     * @param attritionRate 损耗率
//     * @return Result
//     */
//    @PostMapping("/attritionRate/save")
//    public Result saveAttritionRate(AttritionRate attritionRate) {
//        attritionRateService.saveAttritionRate(attritionRate);
//        return ResultUtil.success();
//    }
//
//    /**
//     * 删除损耗率
//     * @param id ID
//     * @return Result
//     */
//    @PostMapping("/attritionRate/del")
//    public Result delAttritionRate(long id) {
//        attritionRateService.delAttritionRate(id);
//        return ResultUtil.success();
//    }
//
//    /**
//     * 条件获取损耗率
//     * @return PageResult
//     */
//    @PostMapping("/attritionRate")
//    public PageResult getAttritionRate(String venderCode, String vender, String materialGroup, String material, boolean isEffect) {
//        return ResultUtil.successPage(attritionRateService.getAttritionRate(venderCode, vender, materialGroup, material, isEffect));
//    }
//
//    /**
//     * EXCEL上传损耗率
//     * @param excel EXCEL
//     * @return Result
//     */
//    @PostMapping(value = "/attritionRate/importAttritionRate")
//    public Result importVenderArrivalPlan(@RequestParam("file") MultipartFile excel) throws Exception {
//        attritionRateService.importAttritionRate(excel.getInputStream(), excel.getOriginalFilename());
//        return ResultUtil.success();
//    }
//}
