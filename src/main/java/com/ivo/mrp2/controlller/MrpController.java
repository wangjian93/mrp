package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.Demand;
import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpMaterial;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.ArrayList;
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

    private MrpService2 mrpService;


    private DemandService demandService;

    private BomService bomService;

    private InventoryService inventoryService;

    private MaterialLossRateService materialLossRateService;

    private MrpMaterialService mrpMaterialService;

    private DpsService dpsService;


    @Autowired
    public MrpController(MrpService2 mrpService,
                         DemandService demandService,
                         BomService bomService,
                         InventoryService inventoryService,
                         MaterialLossRateService materialLossRateService,
                         MrpMaterialService mrpMaterialService,
                         DpsService dpsService) {
        this.mrpService = mrpService;
        this.demandService = demandService;
        this.bomService = bomService;
        this.inventoryService = inventoryService;
        this.materialLossRateService = materialLossRateService;
        this.mrpMaterialService = mrpMaterialService;
        this.dpsService = dpsService;
    }

    /**
     * 获取MRP版本
     * @return Result
     */
    @RequestMapping("/getMrpVer")
    public Result getMrpVer() {
        return ResultUtil.success(mrpService.getMrpVerStr());
    }

    /**
     * 分页获取所有的MRP版本信息
     * @return PageResult
     */
    @RequestMapping("/getAllMrpVerInfo")
    public PageResult getAllMrpVerInfo(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit) {
        Page<MrpVer> p = mrpService.getMrpVer(page-1, limit);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    /**
     * 获取MRP版本的信息
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getMrpVerInfo")
    public Result getMrpVerInfo(@RequestParam String mrpVer) {
        MrpVer m = mrpService.getMrpVer(mrpVer);
        return ResultUtil.success(m);
    }

    /**
     * 获取MRP的日期区间日历
     * @param mrpVer mrp版本
     * @return Result
     */
    @RequestMapping("/getMrpCalendar")
    public Result getMrpCalendar(@RequestParam String mrpVer) {
        List list = mrpService.getMrpCalendarList(mrpVer);
        List<String> days = DateUtil.format_(list);
        String[] weeks = DateUtil.getWeekDay_(list);
        List<String> months = DateUtil.getMonthBetween((Date) list.get(0), (Date) list.get(list.size()-1));
        Map<String, Object> map = new HashMap<>();
        map.put("days", days);
        map.put("weeks", weeks);
        map.put("months", months);
        return ResultUtil.success(map);
    }

    /**
     * 分页获取MRP的数据
     * @return Result
     */
    @RequestMapping("/getMrpData")
    public PageResult getMrpData(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                                 @RequestParam String mrpVer,
                                 @RequestParam(defaultValue = "") String material,
                                 @RequestParam(defaultValue = "") String product,
                                 @RequestParam(defaultValue = "") String materialGroup) {

        Page<MrpMaterial> materialPage = mrpMaterialService.getPageMrpData(page, limit, mrpVer, product, materialGroup, material);
        List<MrpMaterial> mrpMaterialList = materialPage.getContent();
        long count = materialPage.getTotalElements();


        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Integer> cursor = new HashMap<>();   //游标
        List<Date> dateList = mrpService.getMrpCalendarList(mrpVer);
        List<String> months = DateUtil.getMonthBetween(dateList.get(0), dateList.get(dateList.size()-1));
        List<String> materialList = new ArrayList<>();
        for(MrpMaterial mrpMaterial :  mrpMaterialList) {
            Map<String, Object> map = new HashMap<>();
            map.put("plant", mrpMaterial.getPlant());
            map.put("material", mrpMaterial.getMaterial());
            map.put("lossRate", mrpMaterial.getLossRate());
            map.put("goodInventoryQty", mrpMaterial.getGoodInventory());
            map.put("dullInventoryQty", mrpMaterial.getDullInventory());
            map.put("materialName", mrpMaterial.getMaterialName());
            map.put("products", mrpMaterial.getProducts());
            map.put("materialGroup", mrpMaterial.getMaterialGroup());
            for(String month : months) {
                Map<String, Double> subMap = new HashMap<>();
                subMap.put("demandQty", 0D);
                subMap.put("lossQty", 0D);
                subMap.put("arrivalQty", 0D);
                subMap.put("balanceQty", 0D);
                map.put(month, subMap);
            }
            mapList.add(map);
            cursor.put(mrpMaterial.getMaterial(), mapList.size()-1);

            materialList.add(mrpMaterial.getMaterial());
        }

        // 每月的最后一天
        Map<String, Date> lastDays = new HashMap<>();
        for(String month : months) {
            lastDays.put(month, null);
        }

        List<MrpData> mrpDataList = mrpService.getMrpData(mrpVer, materialList);
        if(mrpDataList != null && mrpDataList.size()>0) {
            for(MrpData mrpData : mrpDataList) {
                Map<String, Object> map = mapList.get(cursor.get(mrpData.getMaterial()));
                Map<String, Object> mrpDataMap = new HashMap<>();
                mrpDataMap.put("demandQty", mrpData.getDemandQty());
                mrpDataMap.put("lossQty", mrpData.getLossQty());
                mrpDataMap.put("arrivalQty", mrpData.getArrivalQty());
                mrpDataMap.put("balanceQty", mrpData.getBalanceQty());
                map.put(mrpData.getFabDate().toString(), mrpDataMap);
                // 计算月份汇总
                String month_ = mrpData.getFabDate().toString().substring(0, 7);
                Map subMap = (Map) map.get(month_);
                if(subMap == null) continue;
                subMap.put("demandQty", DoubleUtil.sum( (double) subMap.get("demandQty"),  mrpData.getDemandQty()) );
                subMap.put("lossQty", DoubleUtil.sum( (double) subMap.get("lossQty"),  mrpData.getLossQty()));
                subMap.put("arrivalQty", DoubleUtil.sum( (double) subMap.get("arrivalQty"),  mrpData.getArrivalQty()) );
                if(lastDays.get(month_)==null || (lastDays.get(month_)).before(mrpData.getFabDate()) ) {
                    subMap.put("balanceQty", mrpData.getBalanceQty());
                }
            }
        }

        return ResultUtil.successPage(mapList, count);
    }


    /**
     * 获取材料需求量的数据来源详细
     * @return PageResult
     */
    @RequestMapping("/getMaterialDemandDetail")
    public PageResult getMaterialDemandDetail(@RequestParam String mrpVer, @RequestParam String material, @RequestParam Date fabDate) {
        MrpVer mrp = mrpService.getMrpVer(mrpVer);
        String dpsVer = mrp.getDpsVer();
        List<Demand> demandList = demandService.getDemand(dpsVer, fabDate, material);
        return ResultUtil.successPage(demandList);
    }


    /**
     * 获取MRP版本的机种
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getProduct")
    public Result getProduct(@RequestParam String mrpVer) {
        MrpVer m = mrpService.getMrpVer(mrpVer);
        return ResultUtil.success(dpsService.getProduct(m.getDpsVer()));
    }

    /**
     * 获取MRP版本的料号
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getMaterial")
    public Result getMaterial(@RequestParam String mrpVer) {
        return ResultUtil.success(mrpMaterialService.getMaterial(mrpVer));
    }

    /**
     * 获取MRP版本的物料组
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getMaterialGroup")
    public Result getMaterialGroup(@RequestParam String mrpVer) {
        return ResultUtil.success(mrpMaterialService.getMaterialGroup(mrpVer));
    }

    /**
     * 修改结余量
     * @param mrpVer MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @param balanceQty 结余量
     * @return Result
     */
    @RequestMapping("/updateBalanceQty")
    public Result updateBalanceQty(@RequestParam String mrpVer, @RequestParam String material, @RequestParam Date fabDate,
                                   @RequestParam double balanceQty) {
        MrpData mrpData = mrpService.getMrpData(mrpVer, material, fabDate);
        mrpData.setBalanceQty_(balanceQty-mrpData.getBalanceQty()+mrpData.getBalanceQty_());
        mrpService.updateBalanceQty(mrpData);
        mrpService.computeBalance(mrpVer, material);
        return ResultUtil.success();
    }
}
