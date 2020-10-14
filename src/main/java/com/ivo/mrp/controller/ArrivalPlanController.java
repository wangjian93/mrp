package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.ArrivalPlan;
import com.ivo.mrp.service.ArrivalPlanService;
import com.ivo.mrp.service.MrpService;
import com.ivo.mrp.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 供应商到货计划接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商到货计划接口")
@RestController
@RequestMapping("/mrp/arrivalPlan")
public class ArrivalPlanController {

    private MrpService mrpService;

    private ArrivalPlanService arrivalPlanService;

    private SupplierService supplierService;

    @Autowired
    public ArrivalPlanController(MrpService mrpService, ArrivalPlanService arrivalPlanService, SupplierService supplierService) {
        this.mrpService = mrpService;
        this.arrivalPlanService = arrivalPlanService;
        this.supplierService =supplierService;
    }

    @ApiOperation("获取MRP料号的供应商到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getArrivalQtyByMaterial")
    public Result getArrivalQtyByMaterial(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List list = arrivalPlanService.getArrivalPlan(fab, material, fabDate);
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("获取MRP料号的供应商到货计划2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getArrivalQtyByMaterial2")
    public Result getArrivalQtyByMaterial2(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List<ArrivalPlan> list = arrivalPlanService.getArrivalPlan(fab, material, fabDate);
        Set<String> supplierSet = new HashSet<>();
        for(ArrivalPlan arrivalPlan : list) {
            supplierSet.add(arrivalPlan.getSupplierCode());
        }

        List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
        for(Supplier supplier : supplierList) {
            if(supplierSet.contains(supplier.getSupplierCode())) continue;

            ArrivalPlan arrivalPlan = new ArrivalPlan();
            arrivalPlan.setFab(mrpVer.getFab());
            arrivalPlan.setArrivalQty(0);
            arrivalPlan.setSupplierCode(supplier.getSupplierCode());
            arrivalPlan.setSupplierSname(supplier.getSupplierSname());
            arrivalPlan.setMaterial(material);
            arrivalPlan.setFabDate(fabDate);
            list.add(arrivalPlan);
        }
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("批量保存供应商到货计划")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsons", value = "JSON数据", required = true)
    })
    @PostMapping("/batchSaveArrivalQty")
    public Result batchSaveArrivalQty(String jsons) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map> mapList = objectMapper.readValue(jsons, new TypeReference<List<Map>>() {});
            arrivalPlanService.batchSaveArrivalPlan(mapList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("JSON数据解析失败，"+e.getMessage());
        }
        return ResultUtil.success("到货计划保存成功");
    }
}
