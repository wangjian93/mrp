package com.ivo.mrp2.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.Supplier;
import com.ivo.mrp2.service.SupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 供应商信息接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商信息接口")
@RestController
@RequestMapping("/mrp/supplier")
public class SupplierController {

    private SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * 获取供应商名
     * @param supplier 供应商
     * @limit 最大条数
     * @return Result
     */
    @ApiOperation("获取机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "supplier", value = "模糊查询供应商"),
            @ApiImplicitParam(name = "limit", value = "最大条数", defaultValue = "50")
    })
    @GetMapping("/list")
    public PageResult getSupplier(@RequestParam(defaultValue = "") String supplier, @RequestParam(defaultValue = "50") int limit) {
        Page page = supplierService.searchSupplier(supplier, limit);
        return ResultUtil.successPage(page.getContent(), page.getTotalElements());
    }

    /**
     * 获取供应商名
     * @param supplier 供应商
     * @limit 最大条数
     * @return Result
     */
    @ApiOperation("获取机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "supplier", value = "模糊查询供应商"),
            @ApiImplicitParam(name = "limit", value = "最大条数", defaultValue = "50")
    })
    @GetMapping("/list2")
    public Result getSupplier2(@RequestParam(defaultValue = "") String supplier, @RequestParam(defaultValue = "50") int limit) {
        Page page = supplierService.searchSupplier(supplier, limit);
        List<Supplier> supplerList = page.getContent();
        List<String> stringList = new ArrayList<>();
        for(Supplier obj : supplerList) {
            stringList.add(obj.getName());
        }
        return ResultUtil.success(stringList);
    }

    /**
     * 修改供应商简称
     * @param supplierCode 供应商ID
     * @param sName 简称
     * @return Result
     */
    @ApiImplicitParams({
            @ApiImplicitParam(name = "supplierCode", value = "供应商ID"),
            @ApiImplicitParam(name = "sName", value = "简称")
    })
    @ApiOperation("获取机种")
    @GetMapping("/editName")
    public Result editName(String supplierCode, String sName) {
        supplierService.updateSName(supplierCode, sName);
        return ResultUtil.success();
    }

    @ApiOperation("分页查询供应商信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "limit", value = "分页大小"),
            @ApiImplicitParam(name = "supplier", value = "供应商查询")
    })
    @GetMapping("/getPageSupplier")
    public PageResult getPageSupplier(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit, String supplier) {
        Page p = supplierService.getPageSupplier(page-1, limit, supplier);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }
}
