package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.MaterialSupplierService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 材料与供应商接口
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/materialSupplier")
@Api(tags = "材料与供应商接口")
public class MaterialSupplierController {

    private MaterialSupplierService materialSupplierService;

    @Autowired
    public MaterialSupplierController(MaterialSupplierService materialSupplierService) {
        this.materialSupplierService = materialSupplierService;
    }

    /**
     * 分页查询材料与供应商信息
     * @param page 页数
     * @param limit 分页大小
     * @param material 料号
     * @param supplier 供应商名字
     * @return PageResult
     */
    @ApiOperation("分页查询材料与供应商信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "supplier", value = "供应商名字")
    })
    @GetMapping("/list")
    public PageResult listMaterialSupplier(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "50") int limit,
                                           @RequestParam(defaultValue = "") String material,
                                           @RequestParam(defaultValue = "") String supplier) {
        Page<Map> mapPage = materialSupplierService.getPageMaterialSupplier(page-1, limit, material, supplier);
        return ResultUtil.successPage(mapPage.getContent(), mapPage.getTotalElements());
    }


    /**
     * 添加材料与供应商
     * @param material 料号
     * @param supplierCode 供应商ID
     * @return Result
     */
    @ApiOperation("添加材料与供应商")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "material", value = "料号", required = true),
            @ApiImplicitParam(name = "supplierCode", value = "供应商ID", required = true)
    })
    @GetMapping("/add")
    public Result add(String material, String supplierCode) {
        materialSupplierService.add(material, supplierCode);
        return ResultUtil.success();
    }

    /**
     * 删除材料与供应商
     * @param materialSupplierJsons  删除材料与供应商JSON数据
     * @return Result
     */
    @ApiOperation("删除材料与供应商")
    @ApiImplicitParam(name = "materialSupplierJsons", value = "删除材料与供应商JSON数据", required = true)
    @PostMapping("/delete")
    public Result delete(String materialSupplierJsons) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Map> list = objectMapper.readValue(materialSupplierJsons, new TypeReference<List<Map>>() {});
            if(list != null && list.size()>0) {
                for(Map map : list) {
                    materialSupplierService.delete((String) map.get("material"), (String) map.get("supplierCode"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.error(e.getMessage());
        }
        return ResultUtil.success();
    }
}
