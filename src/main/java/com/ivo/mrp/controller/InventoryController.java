package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/inventory")
public class InventoryController {

    private InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping("/getPageInventory")
    public Result getPageInventory(@RequestParam(required = false, defaultValue = "1") int page,
                                   @RequestParam(required = false, defaultValue = "50") int limit,
                                    Date fabDate,
                                   @RequestParam(required = false, defaultValue = "") String material) {
        Page p = inventoryService.getPageInventory(page-1, limit, fabDate, material);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }
}
