package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.Allocation;
import com.ivo.mrp.service.AllocationService;
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
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商数量分配接口")
@RestController
@RequestMapping("/mrp/allocation")
public class AllocationController {

    private MrpService mrpService;

    private AllocationService allocationService;

    private SupplierService supplierService;

    @Autowired
    public AllocationController(MrpService mrpService, AllocationService allocationService, SupplierService supplierService) {
        this.mrpService = mrpService;
        this.allocationService = allocationService;
        this.supplierService = supplierService;
    }

    @ApiOperation("获取供应商的数量分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getAllocationQtyByMaterial")
    public Result getAllocationQtyByMaterial(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List list = allocationService.getAllocation(fab, material, fabDate);
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("获取供应商的数量分配2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getAllocationQtyByMaterial2")
    public Result getAllocationQtyByMaterial2(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List<Allocation> list = allocationService.getAllocation(fab, material, fabDate);
        Set<String> supplierSet = new HashSet<>();
        for(Allocation allocation : list) {
            supplierSet.add(allocation.getSupplierCode());
        }

        List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
        for(Supplier supplier : supplierList) {
            if(supplierSet.contains(supplier.getSupplierCode())) continue;

            Allocation allocation = new Allocation();
            allocation.setFab(mrpVer.getFab());
            allocation.setAllocationQty(0);
            allocation.setSupplierCode(supplier.getSupplierCode());
            allocation.setSupplierSname(supplier.getSupplierSname());
            allocation.setMaterial(material);
            allocation.setFabDate(fabDate);
            list.add(allocation);
        }
        return ResultUtil.successPage(list, list.size());
    }

    @ApiOperation("批量保存供应商的数量分配")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsons", value = "JSON数据", required = true)
    })
    @PostMapping("/batchSaveAllocation")
    public Result batchSaveAllocation(String jsons) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map> mapList = objectMapper.readValue(jsons, new TypeReference<List<Map>>() {});
            allocationService.batchSaveAllocation(mapList);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error("JSON数据解析失败，"+e.getMessage());
        }
        return ResultUtil.success("保存供应商的数量分配成功");
    }
}
