package com.ivo.mrp2.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机种信息接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "机种信息接口")
@RestController
@RequestMapping("/mrp/product")
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 同步机种信息
     * @return Result
     */
    @ApiOperation("同步机种信息")
    @GetMapping("/sync")
    public Result syncProduct() {
        productService.syncProduct();
        return ResultUtil.success("同步完成");
    }

    /**
     * 获取机种
     * @param product 机种
     * @limit 最大条数
     * @return Result
     */
    @ApiOperation("获取机种")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "product", value = "模糊查询机种"),
            @ApiImplicitParam(name = "limit", value = "最大条数", defaultValue = "100")
    })
    @GetMapping("/list")
    public Result getProduct(@RequestParam(defaultValue = "") String product, @RequestParam(defaultValue = "100") int limit) {
        return ResultUtil.success(productService.searchProduct(product, limit));
    }
}
