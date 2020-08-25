package com.ivo.mrp2.controller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.common.utils.StringUtil;
import com.ivo.mrp2.entity.Demand;
import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpDataMaster;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
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
@Api(tags = "MRP接口")
public class MrpController {

    private MrpService mrpService;


    private DemandService demandService;

    private BomService bomService;

    private InventoryService inventoryService;

    private MaterialLossRateService materialLossRateService;

    private MrpDataMasterService mrpDataMasterService;

    private DpsService dpsService;

    private MaterialSubstituteService materialSubstituteService;

    private MrpService2 mrpService2;


    @Autowired
    public MrpController(MrpService mrpService,
                         DemandService demandService,
                         BomService bomService,
                         InventoryService inventoryService,
                         MaterialLossRateService materialLossRateService,
                         MrpDataMasterService mrpDataMasterService,
                         DpsService dpsService,
                         MaterialSubstituteService materialSubstituteService,
                         MrpService2 mrpService2) {
        this.mrpService = mrpService;
        this.demandService = demandService;
        this.bomService = bomService;
        this.inventoryService = inventoryService;
        this.materialLossRateService = materialLossRateService;
        this.mrpDataMasterService = mrpDataMasterService;
        this.dpsService = dpsService;
        this.materialSubstituteService = materialSubstituteService;
        this.mrpService2 = mrpService2;
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
                                 @RequestParam(defaultValue = "") String materialGroup,
                                 @RequestParam(defaultValue = "") String supplier
                                 ) {

        Page<MrpDataMaster> materialPage = mrpDataMasterService.getPageMrpData(page-1, limit, mrpVer, product, materialGroup, material, supplier);
        List<MrpDataMaster> mrpDataMasterList = materialPage.getContent();
        long count = materialPage.getTotalElements();


        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Integer> cursor = new HashMap<>();   //游标
        List<Date> dateList = mrpService.getMrpCalendarList(mrpVer);
        List<String> months = DateUtil.getMonthBetween(dateList.get(0), dateList.get(dateList.size()-1));
        List<String> materialList = new ArrayList<>();
        for(MrpDataMaster mrpDataMaster : mrpDataMasterList) {
            Map<String, Object> map = new HashMap<>();
            map.put("plant", mrpDataMaster.getPlant());
            map.put("material", mrpDataMaster.getMaterial());
            map.put("lossRate", mrpDataMaster.getLossRate());
            map.put("goodInventoryQty", mrpDataMaster.getGoodInventory());
            map.put("dullInventoryQty", mrpDataMaster.getDullInventory());
            map.put("materialName", mrpDataMaster.getMaterialName());
            map.put("products", mrpDataMaster.getProducts());
            map.put("materialGroup", mrpDataMaster.getMaterialGroup());
            map.put("supplers", mrpDataMaster.getSupplier());
            for(String month : months) {
                Map<String, Double> subMap = new HashMap<>();
                subMap.put("demandQty", 0D);
                subMap.put("lossQty", 0D);
                subMap.put("arrivalQty", 0D);
                subMap.put("balanceQty", 0D);
                map.put(month, subMap);
            }
            mapList.add(map);
            cursor.put(mrpDataMaster.getMaterial(), mapList.size()-1);

            materialList.add(mrpDataMaster.getMaterial());
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
                mrpDataMap.put("shortQt", mrpData.getShortQty());
                mrpDataMap.put("memo", mrpData.getMemo());
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
    public Result getProduct(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String search) {
        return ResultUtil.success(mrpDataMasterService.getProduct(mrpVer, search));
    }

    /**
     * 获取MRP版本的料号
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getMaterial")
    public Result getMaterial(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String search) {
        return ResultUtil.success(mrpDataMasterService.getMaterial(mrpVer, search));
    }

    /**
     * 获取MRP版本的gys
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getSupplier")
    public Result getSupplier(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String search) {
        return ResultUtil.success(mrpDataMasterService.getSupplier(mrpVer, search));
    }

    /**
     * 获取MRP版本的物料组
     * @param mrpVer MRP版本
     * @return Result
     */
    @RequestMapping("/getMaterialGroup")
    public Result getMaterialGroup(@RequestParam String mrpVer, @RequestParam(defaultValue = "") String search) {
        return ResultUtil.success(mrpDataMasterService.getMaterialGroup(mrpVer, search));
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
                                   @RequestParam double balanceQty, @RequestParam String memo) {
        MrpData mrpData = mrpService.getMrpData(mrpVer, material, fabDate);
//        mrpData.setBalanceQtyModify(balanceQty);
        mrpData.setBalanceQty(balanceQty);
        mrpData.setMemo(memo);
        mrpService.updateBalanceQty(mrpData);
        mrpService.computeBalance(mrpVer, material);
        return ResultUtil.success();
    }

    /**
     * 获取结余量信息
     * @param mrpVer MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return Result
     */
    @RequestMapping("/getBalanceQty")
    public Result getBalanceQty(@RequestParam String mrpVer, @RequestParam String material, @RequestParam Date fabDate) {
        MrpData mrpData = mrpService.getMrpData(mrpVer, material, fabDate);
        return ResultUtil.success(mrpData);
    }


    /**
     * MRP执行命令
     * @return Result
     */
    @RequestMapping("/command")
    public Result command(@RequestParam String command, @RequestParam(required = false) String dpsVer,
                          @RequestParam(required = false) String mrpVer) {
        if(StringUtils.equalsIgnoreCase(command, "dps")) {
            dpsService.syncDps();
            return ResultUtil.success("同步DPS完成");
        }

        if(StringUtils.equalsIgnoreCase(command, "bom")) {
            bomService.syncBom();
            return ResultUtil.success("同步BOM完成");
        }

        if(StringUtils.equalsIgnoreCase(command, "mrp") && StringUtils.isNotEmpty(dpsVer)) {
            String mrpVer_ = mrpService.generateMrpVer(dpsVer);
            mrpService.computeDemand(mrpVer_);
            mrpService.generateMrpData(mrpVer_);
            mrpService.computeLossQty(mrpVer_);
            mrpService.computeArrivalQty(mrpVer_);
            mrpService.computeBalance(mrpVer_);
            return ResultUtil.success("计算MRP完成");
        }

        if(StringUtils.equalsIgnoreCase(command, "mrp_") && StringUtils.isNotEmpty(mrpVer)) {
            mrpService.computeDemand(mrpVer);
            mrpService.generateMrpData(mrpVer);
            mrpService.computeLossQty(mrpVer);
            mrpService.computeArrivalQty(mrpVer);
            mrpService.computeBalance(mrpVer);
            return ResultUtil.success("更新MRP完成");
        }

        return ResultUtil.error("指令错误");
    }

    @ApiOperation("选择DPS/MPS版本运算MRP")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dpsVer", value = "DPS版本", required = true),
            @ApiImplicitParam(name = "mpsVer", value = "MPS版本")
    })
    @PostMapping("/runMrp")
    public Result runMrp(String dpsVer, String mpsVer) throws IOException, ClassNotFoundException {
        String[] dpsVers = null;
        if(StringUtils.isNotEmpty(dpsVer)) {
            dpsVers = dpsVer.split(",");
        }
        String[] mpsVers = null;
        if(StringUtils.isNotEmpty(mpsVer)) {
            mpsVers = mpsVer.split(",");
        }
        String ver =  mrpService2.runMrp(dpsVers, mpsVers);
        return ResultUtil.success(ver);
    }
}
