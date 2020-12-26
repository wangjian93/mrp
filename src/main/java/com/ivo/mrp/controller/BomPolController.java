package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.pol.BomPolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/bomPol")
public class BomPolController {

    private BomPolService bomPolService;

    @Autowired
    public BomPolController(BomPolService bomPolService) {
        this.bomPolService = bomPolService;
    }

    @GetMapping("/queryBomPol")
    public Result queryBomPol(@RequestParam(required = false, defaultValue = "1") int page,
                              @RequestParam(required = false, defaultValue = "50") int limit,
                              @RequestParam(required = false, defaultValue = "") String searchProduct) {
        Page p = bomPolService.queryBomPol(page-1, limit, searchProduct);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }
}
