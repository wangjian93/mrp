package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.service.ActualArrivalService;
import com.ivo.mrp.service.MrpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "供应商实际到货")
@RestController
@RequestMapping("/mrp/actualArrival")
public class ActualArrivalController {

    private ActualArrivalService actualArrivalService;
    private MrpService mrpService;

    @Autowired
    public ActualArrivalController(ActualArrivalService actualArrivalService, MrpService mrpService ) {
        this.actualArrivalService = actualArrivalService;
        this.mrpService = mrpService;
    }

    @ApiOperation("查询实际到货数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "fabDate", value = "日期"),
            @ApiImplicitParam(name = "material", value = "料号"),
            @ApiImplicitParam(name = "supplierCode", value = "供应商"),
            @ApiImplicitParam(name = "fab", value = "工厂")
    })
    @GetMapping("/queryActualArrival")
    public Result queryActualArrival(@RequestParam(required = false, defaultValue = "1") int page,
                                     @RequestParam(required = false, defaultValue = "50") int limit,
                                     @RequestParam(required = false, defaultValue = "") String fabDate,
                                     @RequestParam(required = false, defaultValue = "") String material,
                                     @RequestParam(required = false, defaultValue = "") String materialGroup,
                                     @RequestParam(required = false, defaultValue = "") String supplierCode,
                                     @RequestParam(required = false, defaultValue = "") String fab) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if(StringUtils.isNotEmpty(fabDate)) {
            try {
                date = new Date(sdf.parse(fabDate).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Page p = actualArrivalService.queryActualArrival(page-1, limit, date, materialGroup, material, supplierCode, fab);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @GetMapping("/getActualArrival")
    public Result getActualArrival(String ver, String material, Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        List list = actualArrivalService.getActualArrival(fabDate, material, fab);
        return ResultUtil.successPage(list, list.size());
    }
}
