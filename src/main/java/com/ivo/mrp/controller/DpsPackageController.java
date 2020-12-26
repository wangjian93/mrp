package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.cell.DpsCellOutputName;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.DpsCellProduct;
import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.service.DpsService;
import com.ivo.mrp.service.packageing.BomPackageService;
import com.ivo.mrp.service.packageing.DpsPackageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/dpsPackage")
public class DpsPackageController {

    private DpsPackageService dpsPackageService;
    private BomPackageService bomPackageService;
    private DpsService dpsService;

    @Autowired
    public DpsPackageController(DpsPackageService dpsPackageService,
                                BomPackageService bomPackageService,DpsService dpsService) {
        this.dpsPackageService = dpsPackageService;
        this.bomPackageService = bomPackageService;
        this.dpsService = dpsService;
    }

    @GetMapping("/getPageDpsPackageProduct")
    public Result getPageDpsProduct(String ver,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "50") int limit,
                                    @RequestParam(required = false, defaultValue = "") String product) {
        Page<Map> p = dpsPackageService.getPagePackageId(ver, page-1, limit, product);
        List<Map> mapList = new ArrayList<>();
        List<Map> pml = p.getContent();
        for(Map pm : pml) {
            String packageId = (String) pm.get("packageId");
            String product_ = (String) pm.get("product");
            String type = (String) pm.get("type");
            String linkQty = (String) pm.get("linkQty");
            Map<String, Object> map = new HashMap<>();
            map.put("packageId", packageId);
            map.put("product", product_);
            map.put("type", type);
            map.put("linkQty", linkQty);
            List<DpsPackage> list = dpsPackageService.getDpsPackage(ver, packageId);
            for(Object object : list) {
                DpsPackage dpsPackage = (DpsPackage) object;
                double demandQty = dpsPackage.getDemandQty();
                String day = dpsPackage.getFabDate().toString();
                String month = day.substring(0,7);
                map.put(day, dpsPackage);
                map.putIfAbsent(month, 0D);
                map.put(month, DoubleUtil.rounded((double)map.get(month)+demandQty, 3));
            }
            mapList.add(map);
        }
        return ResultUtil.successPage(mapList, p.getTotalElements());
    }

    @GetMapping("/getDpsSplitData")
    public Result getDpsSplitData(String ver, String product) {
        List<DpsCellProduct> dpsCellProductList = dpsPackageService.getDpsCellProduct(ver, product);

        List<Map> mapList = new ArrayList<>();
        Map dps_pc_Map = new HashMap();
        for(DpsCellProduct dpsCellProduct : dpsCellProductList) {
            dps_pc_Map.put(dpsCellProduct.getFabDate().toString(), dpsCellProduct.getDemandQty());
        }
        mapList.add(dps_pc_Map);

        List<BomPackage> bomPackageList = bomPackageService.getBomPackage(product);
        for(BomPackage bomPackage : bomPackageList) {
            Map subMap = new HashMap();
            subMap.put("packageId", bomPackage.getId());
            subMap.put("product", bomPackage.getProduct());
            subMap.put("type", bomPackage.getType());
            subMap.put("linkQty", bomPackage.getLinkQty());
            List<DpsPackage> dpsPackageList = dpsPackageService.getDpsPackage(ver, bomPackage.getId());
            for(DpsPackage dpsPackage : dpsPackageList) {
                subMap.put(dpsPackage.getFabDate().toString(), dpsPackage.getDemandQty());

            }
            mapList.add(subMap);
        }

        return ResultUtil.successPage(mapList, mapList.size());
    }

    @PostMapping("/submitDpsSplitData")
    public Result submitDpsSplitData(String ver, String product, String jsonData) {
        List<Map> mapList;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            mapList = objectMapper.readValue(jsonData, new TypeReference<List<Map>>() {});
        } catch (IOException e) {
            return ResultUtil.error("提交数据解析错误");
        }

        //删除旧数据
        dpsPackageService.delete(dpsPackageService.getDpsPackageByProduct(ver, product));

        List<DpsPackage> dpsPackageList = new ArrayList<>();
        List<Date> dateList = dpsService.getDpsCalendar(ver);
        for(Map map : mapList) {
            String packageId = (String) map.get("packageId");
            if(packageId == null) continue;
            BomPackage bomPackage = bomPackageService.getBomPackageById(packageId);
            for(Date fabDate : dateList) {
                Object object = map.get(fabDate.toString());
                if(object == null) continue;
                double demandQty = 0;
                if(object instanceof Double || object instanceof Integer) {
                    demandQty = (Double) object;
                } else {
                    if(StringUtils.isEmpty((String)object)) {
                        demandQty = 0;
                    } else {
                        demandQty = Double.parseDouble((String)object);
                    }
                }
                if(demandQty>0) {
                    DpsPackage dpsPackage = new DpsPackage();
                    dpsPackage.setVer(ver);
                    dpsPackage.setFabDate(fabDate);
                    dpsPackage.setDemandQty(demandQty);
                    dpsPackage.setPackageId(packageId);
                    dpsPackage.setProduct(bomPackage.getProduct());
                    dpsPackage.setType(bomPackage.getType());
                    dpsPackage.setLinkQty(bomPackage.getLinkQty());
                    dpsPackageList.add(dpsPackage);
                }
            }

        }
        dpsPackageService.save(dpsPackageList);
        return ResultUtil.success("提交成功");
    }
}
