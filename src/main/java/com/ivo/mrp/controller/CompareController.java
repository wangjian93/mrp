package com.ivo.mrp.controller;

import com.ivo.common.result.Result;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.MpsVer;
import com.ivo.mrp.entity.direct.ary.DpsAry;
import com.ivo.mrp.entity.direct.ary.MpsAry;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.cell.MpsCell;
import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.entity.direct.lcm.MpsLcm;
import com.ivo.mrp.service.DpsService;
import com.ivo.mrp.service.MpsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Api(tags = "DPS & MPS数据对比")
@RestController
@RequestMapping("/mrp/compare")
public class CompareController {

    private DpsService dpsService;

    private MpsService mpsService;

    @Autowired
    public CompareController(DpsService dpsService, MpsService mpsService) {
        this.dpsService = dpsService;
        this.mpsService = mpsService;
    }

    @ApiOperation("比对DPS与MPS的数据")
    @GetMapping("/compareDpsMps")
    public Result compareDpsMps(String dpsVer, String mpsVer) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String type = dps.getType();
        List<Map> mapList = new ArrayList<>();
        Map<String, Integer> tagMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (type.equals(DpsVer.Type_Lcm)) {
            List<DpsLcm> dpsLcmList = dpsService.getDpsLcm(dpsVer);
            for (DpsLcm dpsLcm : dpsLcmList) {
                String product = dpsLcm.getProduct();
                product = product.split(" ")[0];
                Map map;
                if (tagMap.get(product) == null) {
                    map = new HashMap();
                    map.put("product", product);
                    mapList.add(map);
                    tagMap.put(product, mapList.size() - 1);
                } else {
                    map = mapList.get(tagMap.get(product));
                }
                String month = sdf.format(dpsLcm.getFabDate());
                Map compareMap;
                if (map.get(month) == null) {
                    compareMap = new HashMap();
                    compareMap.put("dpsQty", 0D);
                    compareMap.put("mpsQty", 0D);
                    compareMap.put("compareQty", 0D);
                    map.put(month, compareMap);
                } else {
                    compareMap = (Map) map.get(month);
                }
                double dpsQty = (double) compareMap.get("dpsQty");
                double mpsQty = (double) compareMap.get("mpsQty");
                dpsQty = DoubleUtil.rounded(dpsQty + dpsLcm.getDemandQty(), 3);
                double compareQty = DoubleUtil.rounded(dpsQty - mpsQty, 3);
                compareMap.put("dpsQty", dpsQty);
                compareMap.put("mpsQty", mpsQty);
                compareMap.put("compareQty", compareQty);
            }

            List<MpsLcm> mpsLcmList = mpsService.getMpsLcm(mpsVer);
            for (MpsLcm mpsLcm : mpsLcmList) {
                String product = mpsLcm.getProduct();
                product = product.split(" ")[0];
                Map map;
                if (tagMap.get(product) == null) {
                    map = new HashMap();
                    map.put("product", product);
                    mapList.add(map);
                    tagMap.put(product, mapList.size() - 1);
                } else {
                    map = mapList.get(tagMap.get(product));
                }
                String month = sdf.format(mpsLcm.getFabDate());
                Map compareMap;
                if (map.get(month) == null) {
                    compareMap = new HashMap();
                    compareMap.put("dpsQty", 0D);
                    compareMap.put("mpsQty", 0D);
                    compareMap.put("compareQty", 0D);
                    map.put(month, compareMap);
                } else {
                    compareMap = (Map) map.get(month);
                }
                double dpsQty = (double) compareMap.get("dpsQty");
                double mpsQty = (double) compareMap.get("mpsQty");
                mpsQty = DoubleUtil.rounded(mpsQty+mpsLcm.getDemandQty(), 3);
                double compareQty = DoubleUtil.rounded(dpsQty-mpsQty, 3);
                compareMap.put("dpsQty", dpsQty);
                compareMap.put("mpsQty", mpsQty);
                compareMap.put("compareQty", compareQty);
            }
        } else if (type.equals(DpsVer.Type_Cell)) {
            List<DpsCell> dpsCellList = dpsService.getDpsCell(dpsVer);
            for (DpsCell dpsCell : dpsCellList) {
                String product = dpsCell.getProduct();
                product = product.split(" ")[0];
                Map map;
                if (tagMap.get(product) == null) {
                    map = new HashMap();
                    map.put("product", product);
                    mapList.add(map);
                    tagMap.put(product, mapList.size() - 1);
                } else {
                    map = mapList.get(tagMap.get(product));
                }
                String month = sdf.format(dpsCell.getFabDate());
                Map compareMap;
                if (map.get(month) == null) {
                    compareMap = new HashMap();
                    compareMap.put("dpsQty", 0D);
                    compareMap.put("mpsQty", 0D);
                    compareMap.put("compareQty", 0D);
                    map.put(month, compareMap);
                } else {
                    compareMap = (Map) map.get(month);
                }
                double dpsQty = (double) compareMap.get("dpsQty");
                double mpsQty = (double) compareMap.get("mpsQty");
                dpsQty = DoubleUtil.rounded(dpsQty+dpsCell.getDemandQty(), 3);
                double compareQty = DoubleUtil.rounded(dpsQty-mpsQty, 3);
                compareMap.put("dpsQty", dpsQty);
                compareMap.put("mpsQty", mpsQty);
                compareMap.put("compareQty", compareQty);
            }

            List<MpsCell> mpsCellList = mpsService.getMpsCell(mpsVer);
            for (MpsCell mpsCell : mpsCellList) {
                String product = mpsCell.getProduct();
                product = product.split(" ")[0];
                Map map;
                if (tagMap.get(product) == null) {
                    map = new HashMap();
                    map.put("product", product);
                    mapList.add(map);
                    tagMap.put(product, mapList.size() - 1);
                } else {
                    map = mapList.get(tagMap.get(product));
                }
                String month = sdf.format(mpsCell.getFabDate());
                Map compareMap;
                if (map.get(month) == null) {
                    compareMap = new HashMap();
                    compareMap.put("dpsQty", 0D);
                    compareMap.put("mpsQty", 0D);
                    compareMap.put("compareQty", 0D);
                    map.put(month, compareMap);
                } else {
                    compareMap = (Map) map.get(month);
                }
                double dpsQty = (double) compareMap.get("dpsQty");
                double mpsQty = (double) compareMap.get("mpsQty");
                mpsQty = DoubleUtil.rounded(mpsQty+mpsCell.getDemandQty(), 3);
                double compareQty = DoubleUtil.rounded(dpsQty-mpsQty, 3);
                compareMap.put("dpsQty", dpsQty);
                compareMap.put("mpsQty", mpsQty);
                compareMap.put("compareQty", compareQty);
            }

        } else if (type.equals(DpsVer.Type_Ary)) {
            List<DpsAry> dpsAryList = dpsService.getDpsAry(dpsVer);
            for (DpsAry dpsAry : dpsAryList) {
                String product = dpsAry.getProduct();
                product = product.split(" ")[0];
                Map map;
                if (tagMap.get(product) == null) {
                    map = new HashMap();
                    map.put("product", product);
                    mapList.add(map);
                    tagMap.put(product, mapList.size() - 1);
                } else {
                    map = mapList.get(tagMap.get(product));
                }
                String month = sdf.format(dpsAry.getFabDate());
                Map compareMap;
                if (map.get(month) == null) {
                    compareMap = new HashMap();
                    compareMap.put("dpsQty", 0D);
                    compareMap.put("mpsQty", 0D);
                    compareMap.put("compareQty", 0D);
                    map.put(month, compareMap);
                } else {
                    compareMap = (Map) map.get(month);
                }
                double dpsQty = (double) compareMap.get("dpsQty");
                double mpsQty = (double) compareMap.get("mpsQty");
                dpsQty = DoubleUtil.rounded(dpsQty+dpsAry.getDemandQty(), 3);
                double compareQty = DoubleUtil.rounded(dpsQty-mpsQty, 3);
                compareMap.put("dpsQty", dpsQty);
                compareMap.put("mpsQty", mpsQty);
                compareMap.put("compareQty", compareQty);
            }

            List<MpsAry> mpsAryList = mpsService.getMpsAry(mpsVer);
            for (MpsAry mpsAry : mpsAryList) {
                String product = mpsAry.getProduct();
                product = product.split(" ")[0];
                Map map;
                if (tagMap.get(product) == null) {
                    map = new HashMap();
                    map.put("product", product);
                    mapList.add(map);
                    tagMap.put(product, mapList.size() - 1);
                } else {
                    map = mapList.get(tagMap.get(product));
                }
                String month = sdf.format(mpsAry.getFabDate());
                Map compareMap;
                if (map.get(month) == null) {
                    compareMap = new HashMap();
                    compareMap.put("dpsQty", 0D);
                    compareMap.put("mpsQty", 0D);
                    compareMap.put("compareQty", 0D);
                    map.put(month, compareMap);
                } else {
                    compareMap = (Map) map.get(month);
                }
                double dpsQty = (double) compareMap.get("dpsQty");
                double mpsQty = (double) compareMap.get("mpsQty");
                mpsQty = DoubleUtil.rounded(mpsQty+mpsAry.getDemandQty(), 3);
                double compareQty = DoubleUtil.rounded(dpsQty-mpsQty, 3);
                compareMap.put("dpsQty", dpsQty);
                compareMap.put("mpsQty", mpsQty);
                compareMap.put("compareQty", compareQty);
            }

        }
        return ResultUtil.successPage(mapList, mapList.size());
    }

    @ApiOperation("获取DPS与MPS比对的月份")
    @GetMapping("/getCompareMonth")
    public Result getCompareMonth(String dpsVer, String mpsVer) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        Date startDate = dps.getStartDate();
        Date endDate = dps.getEndDate();
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        if(startDate.after(mps.getStartDate())) {
            startDate = mps.getStartDate();
        }
        if(endDate.before(mps.getEndDate())) {
            endDate = mps.getEndDate();
        }
        List<String> months = DateUtil.getMonthBetween(startDate,endDate);
        return ResultUtil.success(months);
    }
}
