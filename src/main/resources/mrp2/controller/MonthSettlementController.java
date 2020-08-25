package com.ivo.mrp2.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MonthSettlement;
import com.ivo.mrp2.service.MonthSettlementService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 月结功能接口
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/settlement")
@Api(tags = "月结功能接口")
public class MonthSettlementController {

    private MonthSettlementService monthSettlementService;

    @Autowired
    public MonthSettlementController(MonthSettlementService monthSettlementService) {
        this.monthSettlementService = monthSettlementService;
    }

    /**
     * 获取某月份的月结数据
     * @param plant 厂别
     * @param month 月份
     * @return PageResult
     */
    @ApiOperation("获取某月份的月结数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "month", value = "月份", required = true)
    })
    @GetMapping("/getMonthSettlement")
    public PageResult getMonthSettlement(String plant, String month) {
        return ResultUtil.successPage(monthSettlementService.getMonthSettlement(plant, month));
    }

    /**
     * 月结数据提交
     * @param plant 厂别
     * @param month 月份
     * @param product 机种
     * @param materialGroup 物料组
     * @param qty 数量
     * @param settlementDate 放入下月的日期
     * @return Result
     */
    @ApiOperation("月结数据提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "plant", value = "厂别", required = true),
            @ApiImplicitParam(name = "month", value = "月份", required = true),
            @ApiImplicitParam(name = "product", value = "机种", required = true),
            @ApiImplicitParam(name = "materialGroup", value = "物料组", required = true),
            @ApiImplicitParam(name = "qty", value = "数量", required = true),
            @ApiImplicitParam(name = "settlementDate", value = "放入下月的日期", required = true)
    })
    @PostMapping("/submit")
    public Result submit(String plant, String month, String product, String materialGroup, double qty, Date settlementDate) {
        monthSettlementService.save(plant, month, product, materialGroup, qty, settlementDate);
        return ResultUtil.success();
    }

    /**
     * 月结数据删除
     * @param settlementJsons 月结数据
     * @return Result
     */
    @ApiOperation("月结数据删除")
    @ApiImplicitParam(name = "settlementJsons", value = "月结数据", required = true)
    @PostMapping("/delete")
    public Result delete(String settlementJsons) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<MonthSettlement> list = new ArrayList<>();
        try {
            list = objectMapper.readValue(settlementJsons, new TypeReference<List<MonthSettlement>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        monthSettlementService.delete(list);
        return ResultUtil.success();
    }
}
