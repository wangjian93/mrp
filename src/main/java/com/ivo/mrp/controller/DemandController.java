package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.service.DemandService;
import com.ivo.mrp.service.MrpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 需求量接口
 * @author wj
 * @version 1.0
 */
@Api(tags = "需求量接口")
@RestController
@RequestMapping("/mrp/demand")
public class DemandController {

    private DemandService demandService;

    private MrpService mrpService;

    @Autowired
    public DemandController(DemandService demandService, MrpService mrpService) {
        this.demandService = demandService;
        this.mrpService = mrpService;
    }

    @ApiOperation("获取MRP料号的需求明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ver", value = "MRP版本"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "fabDate", value = "日期")
    })
    @GetMapping("/getDemandByMaterial")
    public Result getDemandByMaterial(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        List<Object> list = new ArrayList<>();
        if(type.equals(MrpVer.Type_Lcm)) {
            list.addAll(demandService.getDemandLcm(ver, material, fabDate));
        } else if(type.equals(MrpVer.Type_Cell)) {
            list.addAll(demandService.getDemandCell(ver, material, fabDate));

        } else if(type.equals(MrpVer.Type_Ary)) {
            list.addAll(demandService.getDemandAry(ver, material, fabDate));
            list.addAll(demandService.getDemandAryOc(ver, material, fabDate));
        }
        return ResultUtil.successPage(list, list.size());
    }
}
