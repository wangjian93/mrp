//package com.ivo.mrp.controller;
//
//import com.ivo.common.result.Result;
//import com.ivo.common.utils.DateUtil;
//import com.ivo.common.utils.ResultUtil;
//import com.ivo.mrp.service.DpsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.*;
//
///**
// * DPS & MPS
// * @author wj
// * @version 1.0
// */
//@RestController
//@RequestMapping("/mrp/dps")
//@Slf4j
//public class DpsController {
//
////    private DpsService dpsService;
////
////    @Autowired
////    public DpsController(DpsService dpsService) {
////        this.dpsService = dpsService;
////    }
////
////    /**
////     * 按版本获取DPS数据
////     * @param ver dps版本
////     * @return PageResult
////     */
////    @GetMapping("/getDps/{ver}")
////    public Result getDps(@PathVariable("ver") String ver) {
////        return ResultUtil.success(dpsService.getDps(ver));
////    }
////
////    /**
////     * 获取版本的日历
////     * @param ver dps版本
////     * @return PageResult
////     */
////    @GetMapping("/getCalendar/{ver}")
////    public Result getCalendar(@PathVariable("ver") String ver) {
////        Date[] dates = dpsService.getStartAndEndDate(ver);
////        List<String> dayList = DateUtil.getDays(dates[0], dates[1]);
////        String[] weekDays = DateUtil.getWeekDay_(dayList);
////        Map<String, Object> map = new HashMap<>();
////        map.put("day", dayList.toArray());
////        map.put("week", weekDays);
////        return ResultUtil.success(map);
////    }
////
////    /**
////     * 获取DPS的版本
////     * @return Result
////     */
////    @GetMapping("/getDpsVer")
////    public Result getDpsVer() {
////        return ResultUtil.success(dpsService.getDpsVer());
////    }
//}
