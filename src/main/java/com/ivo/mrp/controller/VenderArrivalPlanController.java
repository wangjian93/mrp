//package com.ivo.mrp.controller;
//
//import com.ivo.common.result.PageResult;
//import com.ivo.common.result.Result;
//import com.ivo.common.utils.ExcelUtil;
//import com.ivo.common.utils.HttpServletUtil;
//import com.ivo.common.utils.ResultUtil;
//import com.ivo.mrp.service.VenderArrivalPlanService;
//import org.apache.poi.ss.usermodel.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.*;
//
///**
// * 供应商到货计划管理
// * @author wj
// * @version 1.0
// */
//@RestController
//@RequestMapping("/mrp")
//public class VenderArrivalPlanController {
//
//    private VenderArrivalPlanService venderArrivalPlanService;
//
//    @Autowired
//    public VenderArrivalPlanController(VenderArrivalPlanService venderArrivalPlanService) {
//        this.venderArrivalPlanService = venderArrivalPlanService;
//    }
//
//    /**
//     * 动态查询条件，根据月份、供应商、料号查询到料计划
//     *
//     * @param month    月份
//     * @param vender   供应商
//     * @param material 料号
//     * @return PageResult
//     */
//    @RequestMapping("/venderArrivalPlan")
//    public PageResult getVenderArrivalPlan(String month, String vender, String material) {
//        return ResultUtil.successPage(venderArrivalPlanService.getVenderArrivalPlan(month, vender, material));
//    }
//
//
//    /**
//     * EXCEL上传供应商到货计划
//     * @param excel EXCEL
//     * @return Result
//     */
//    @PostMapping(value = "/venderArrivalPlan/importVenderArrivalPlan")
//    public Result importVenderArrivalPlan(@RequestParam("file") MultipartFile excel) {
//        try {
//            String month;
//            month = venderArrivalPlanService.importVenderArrivalPlanByMonth(excel.getInputStream(), excel.getOriginalFilename());
//            return ResultUtil.success("成功", month);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResultUtil.error(e.getMessage());
//        }
//    }
//
//    /**
//     * 导出供应商到货计划
//     * @param month 月份
//     * @param vender 供应商
//     * @param material 料号
//     */
//    @RequestMapping(value = "/venderArrivalPlan/exportVenderArrivalPlan")
//    public void exportVenderArrivalPlan(String month, String vender, String material) {
//        HttpServletResponse response = HttpServletUtil.getResponse();
//        String fileName = "供应商到料计划-"+month+".xlsx";
//        try {
//            fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            fileName = UUID.randomUUID().toString() + ".xls";
//        }
//
//        Workbook wk = venderArrivalPlanService.exportVenderArrivalPlan(month, vender, material);
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
//        try {
//            OutputStream os = response.getOutputStream();
//            ExcelUtil.writeWorkbookToOutputStream(wk, os);
//            os.flush();
//            os.close();
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
