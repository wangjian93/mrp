package com.ivo.mrp2.controlller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.service.DpsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DPS请求处理
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/dps")
public class DpsController {

    private DpsService dpsService;

    @Autowired
    public DpsController(DpsService dpsService) {
        this.dpsService = dpsService;
    }

    /**
     * 获取DSP的所有版本
     * @return Result
     */
    @RequestMapping("/getDpsVer")
    public Result getDpsVer() {
        return ResultUtil.success(dpsService.getDpsVer());
    }

    /**
     * 获取DPS的日期区间日历
     * @param dpsVer dps版本
     * @return Result
     */
    @RequestMapping("/getDpsCalendar")
    public Result getDpsCalendar(@RequestParam String dpsVer) {
        List list = dpsService.getDpsCalendarList(dpsVer);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        return ResultUtil.success(map);
    }

    /**
     * 获取DPS的数据
     * @param dpsVer 版本
     * @param product 产品
     * @return Result
     */
    @RequestMapping("/getDps")
    public Result getDps(@RequestParam String dpsVer, @RequestParam(defaultValue = "") String product) {
        if(StringUtils.isEmpty(product)) {
            return ResultUtil.success(dpsService.getDps(dpsVer));
        } else {
            return ResultUtil.success(dpsService.getDps(dpsVer, product));
        }
    }


    /**
     * MC手动Excel上传DPS
     * @param file 文件
     * @return Result
     */
    @RequestMapping("/importDps")
    public Result importDps(@RequestParam("file") MultipartFile file) {
        try {
            dpsService.importDps(file.getInputStream(), file.getOriginalFilename());
        } catch (IOException e) {
            return ResultUtil.error("上传失败：" + e.getMessage());
        }
        return ResultUtil.success();
    }
}
