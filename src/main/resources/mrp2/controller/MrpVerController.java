package com.ivo.mrp2.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.repository.MrpVerRepository;
import com.ivo.mrp2.service.MrpVerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * MRP版本接口
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/mrpVer")
@Api(tags = "MRP版本接口")
public class MrpVerController {

    private MrpVerService mrpVerService;
    private MrpVerRepository mrpVerRepository;

    @Autowired
    public MrpVerController(MrpVerService mrpVerService, MrpVerRepository mrpVerRepository) {
        this.mrpVerService = mrpVerService;
        this.mrpVerRepository = mrpVerRepository;
    }

    @ApiOperation("分页查询MRP版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页数", defaultValue = "1"),
            @ApiImplicitParam(name = "limit", value = "分页大小", defaultValue = "50"),
            @ApiImplicitParam(name = "dpsVer", value = "DPS版本"),
            @ApiImplicitParam(name = "mpsVer", value = "MPS版本"),
            @ApiImplicitParam(name = "plant", value = "厂别"),
            @ApiImplicitParam(name = "fromDate", value = "开始时间"),
            @ApiImplicitParam(name = "toDate", value = "结束时间")
    })
    @GetMapping("/getPageMrpVer")
    public PageResult getPageMrpVer(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "50") int limit,
                                    @RequestParam(required = false) String dpsVer,
                                    @RequestParam(required = false) String mpsVer,
                                    @RequestParam(required = false) String plant,
                                    @RequestParam(required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                    @RequestParam(required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        Page p = mrpVerService.getPageMrpVer(page-1, limit, dpsVer, mpsVer, plant, fromDate, toDate);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    @ApiOperation("获取MRP的日期区间日历")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true),
            @ApiImplicitParam(name = "fromDate", value = "开始时间"),
            @ApiImplicitParam(name = "toDate", value = "结束时间")
    })
    @GetMapping("/getMrpCalendar")
    public Result getMrpCalendar(String mrpVer,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {
        List<Date> dateList = mrpVerService.getMrpCalendarList(mrpVer);
        List<Date> list = new ArrayList<>();
        for(Date date : dateList) {
            if(fromDate != null && date.before(fromDate)) {
                continue;
            }
            if(toDate !=null && date.after(toDate)) {
                continue;
            }
            list.add(date);
        }
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        List<String> months = DateUtil.getMonthBetween(m.getStartDate(), m.getEndDate());
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        map.put("months", months);
        return ResultUtil.success(map);
    }

    @ApiOperation("获取MRP的版本信息")
    @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true)
    @GetMapping("/getMrpVer")
    public Result getMrpVer(String mrpVer) {
        return ResultUtil.success(mrpVerService.getMrpVer(mrpVer));
    }

    @ApiOperation("删除MRP的版本信息")
    @ApiImplicitParam(name = "mrpVer", value = "MRP版本", required = true)
    @GetMapping("/deleteMrp")
    public Result deleteMrp(String mrpVer) {
        mrpVerRepository.delete(mrpVerService.getMrpVer(mrpVer));
        return ResultUtil.success();
    }
}
