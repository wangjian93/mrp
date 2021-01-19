package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.HttpServletUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.InventoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
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


    @GetMapping("/exportExcel")
    public void exportExcel(Date fabDate) throws IOException {
        Workbook workbook = inventoryService.exportExcel(fabDate);
        HttpServletResponse response = HttpServletUtil.getResponse();
        response.setContentType("application/vnd.ms-excel;chartset=utf-8");
        String fileName = "期初库存数据";
        fileName = URLEncoder.encode(fileName, "UTF8");
        response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
        OutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }
}
