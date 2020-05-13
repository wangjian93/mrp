package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.service.MaterialDailyBalanceService;
import com.ivo.mrp2.service.DemandService;
import com.ivo.mrp2.service.MrpService;
import com.ivo.mrp2.service.SupplierArrivalPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MRP请求处理
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp")
public class MrpController {

    private MrpService mrpService;

    private MaterialDailyBalanceService balanceService;

    private SupplierArrivalPlanService arrivalPlanService;

    private DemandService demandService;

    @Autowired
    public MrpController(MrpService mrpService,
                         MaterialDailyBalanceService balanceService,
                         SupplierArrivalPlanService arrivalPlanService,
                         DemandService demandService) {
        this.mrpService = mrpService;
        this.balanceService = balanceService;
        this.arrivalPlanService = arrivalPlanService;
        this.demandService = demandService;
    }

    /**
     * 获取MRP版本
     * @return Result
     */
    @RequestMapping("/getMrpVer")
    public Result getMrpVer() {
        return ResultUtil.success(mrpService.getMrpVer());
    }

    /**
     * 获取MRP的日期区间日历
     * @param mrpVer mrp版本
     * @return Result
     */
    @RequestMapping("/getDpsCalendar")
    public Result getDpsCalendar(@RequestParam String mrpVer) {
        List<Date> list = mrpService.getMrpCalendarList(mrpVer);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        return ResultUtil.success(map);
    }

    /**
     * 获取材料结余数据
     * @param mrpVer mrp版本
     * @param material 材料
     * @return Result
     */
    @RequestMapping("/getBalance")
    public Result getBalance(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String material) {
        if(StringUtils.isEmpty(material)) {
            return  ResultUtil.success(balanceService.getMaterialDailyBalance(mrpVer));
        } else {
            return ResultUtil.success(balanceService.getMaterialDailyBalance(mrpVer, material));
        }
    }

    /**
     * 获取到货计划
     * @param mrpVer mrp版本
     * @param material 材料
     * @return Result
     */
    @RequestMapping("/getArrivalPlan")
    public Result getArrivalPlan(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String material) {
        MrpVer mrp = mrpService.getMrpVer(mrpVer);
        if(mrp == null) return ResultUtil.error("MRP版本不存在");
        if(StringUtils.isEmpty(material)) {
            return  ResultUtil.success(arrivalPlanService.getSupplierArrivalPlan(mrp.getStartDate(), mrp.getEndDate()));
        } else {
            return ResultUtil.success(arrivalPlanService.getSupplierArrivalPlan(mrp.getStartDate(), mrp.getEndDate(), material));
        }
    }

    /**
     * 获取材料的需求量和损耗量
     * @param mrpVer mrp版本
     * @param material 材料
     * @return Result
     */
    @RequestMapping("/getDemandAndLoss")
    public Result getDemandAndLoss(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String material) {
        if(StringUtils.isEmpty(material)) {
            return ResultUtil.success(demandService.getMaterialDailyDemandLoss(mrpVer));
        } else {
            return ResultUtil.success(demandService.getMaterialDailyDemandLoss(mrpVer, material));
        }
    }

    /**
     * 获取材料的需求量和损耗量
     * @param mrpVer mrp版本
     * @param material 材料
     * @return Result
     */
    @RequestMapping("/getDemandTemp")
    public PageResult getDemandTemp(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String material) {
        if(StringUtils.isEmpty(material)) {
            return ResultUtil.successPage(demandService.getMaterialDailyDemandTemp(mrpVer));
        } else {
            return ResultUtil.successPage(demandService.getMaterialDailyDemandTemp(mrpVer, material));
        }
    }
}
