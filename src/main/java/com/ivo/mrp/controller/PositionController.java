package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.service.PositionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "良品呆滞品仓位")
@RestController
@RequestMapping("/mrp/position")
public class PositionController {

    private PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @ApiOperation("获取良品呆滞品仓位")
    @GetMapping("/getPosition")
    public Result getPosition() {
        Map map = new HashMap<>();
        map.put("ivo-good",  positionService.getPositionIvoGood());
        map.put("ivo-dull",  positionService.getPositionIvoDull());
        map.put("ive-good",  positionService.getPositionIveGood());
        map.put("ive-dull",  positionService.getPositionIveDull());
        return ResultUtil.success(map);
    }
}
