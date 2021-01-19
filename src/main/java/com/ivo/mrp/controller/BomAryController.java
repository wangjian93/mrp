package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.direct.ary.BomAry;
import com.ivo.mrp.service.ary.BomAryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/bomAry")
public class BomAryController {

    private BomAryService bomAryService;

    @Autowired
    public BomAryController(BomAryService bomAryService) {
        this.bomAryService = bomAryService;
    }

    @GetMapping("/queryBomProduct")
    public Result queryBomProduct(@RequestParam(required = false, defaultValue = "1") int page,
                                  @RequestParam(required = false, defaultValue = "50") int limit,
                                  @RequestParam(required = false, defaultValue = "") String product) {
        Page p = bomAryService.queryProduct(page-1, limit, product);
        if(p!=null) {
            return ResultUtil.successPage(p.getContent(), p.getTotalElements());
        } else {
            return ResultUtil.successPage(new ArrayList(), 0);
        }
    }

    @GetMapping("/getBom")
    public Result getBom(String product) {
        List<BomAry> bomAryList = bomAryService.getBomAry(product);
        return ResultUtil.success(bomAryList);
    }
}
