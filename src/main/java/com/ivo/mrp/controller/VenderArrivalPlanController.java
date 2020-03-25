package com.ivo.mrp.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.VenderArrivalPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp")
public class VenderArrivalPlanController {

    private VenderArrivalPlanService venderArrivalPlanService;

    @Autowired
    public VenderArrivalPlanController(VenderArrivalPlanService venderArrivalPlanService) {
        this.venderArrivalPlanService = venderArrivalPlanService;
    }

    /**
     * 动态查询条件，根据月份、供应商、料号查询到料计划
     * @param month 月份
     * @param vender 供应商
     * @param material 料号
     * @return PageResult
     */
    @RequestMapping("/venderArrivalPlan")
    public PageResult getVenderArrivalPlan(String month, String vender, String material) {
        return ResultUtil.successPage(venderArrivalPlanService.getVenderArrivalPlan(month, vender, material));
    }



    @PostMapping(value = "/venderArrivalPlan/uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {

        String month;
        try {
            month = venderArrivalPlanService.uploadVenderArrivalPlan(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success("成功", month);
    }
}
