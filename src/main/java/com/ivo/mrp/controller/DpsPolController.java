package com.ivo.mrp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.Result;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp.entity.pol.DpsCellPolProduct;
import com.ivo.mrp.entity.pol.DpsPol;
import com.ivo.mrp.service.DpsService;
import com.ivo.mrp.service.pol.BomPolService;
import com.ivo.mrp.service.pol.DpsPolService;
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
@RequestMapping("/mrp/dpsPol")
public class DpsPolController {

    private DpsPolService dpsPolService;

    private BomPolService bomPolService;

    private DpsService dpsService;

    @Autowired
    public DpsPolController(DpsPolService dpsPolService, BomPolService bomPolService, DpsService dpsService) {
        this.dpsPolService = dpsPolService;
        this.bomPolService = bomPolService;
        this.dpsService = dpsService;
    }

    @GetMapping("/getPageDpsPolProduct")
    public Result getPageDpsProduct(String ver,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "50") int limit,
                                    @RequestParam(required = false, defaultValue = "") String product) {
        Page<Map> p = dpsPolService.getPageProduct(ver, page-1, limit, product);
        List<Map> mapList = new ArrayList<>();
        List<Map> pml = p.getContent();
        for(Map pm : pml) {
            String product_ = (String) pm.get("product");
            String project = (String) pm.get("project");
            Map<String, Object> map = new HashMap<>();
            map.put("product", product_);
            map.put("project", project);
            List<DpsPol> list = dpsPolService.getDpsPol(ver, product_);
            for(DpsPol dpsPol : list) {
                double demandQty = dpsPol.getDemandQty();
                String day = dpsPol.getFabDate().toString();
                String month = day.substring(0,7);
                map.put(day, dpsPol);
                map.putIfAbsent(month, 0D);
                map.put(month, DoubleUtil.rounded((double)map.get(month)+demandQty, 3));
            }
            mapList.add(map);
        }
        return ResultUtil.successPage(mapList, p.getTotalElements());
    }

    @GetMapping("/getDpsSplitData")
    public Result getDpsSplitData(String ver, String project) {
        List<DpsCellPolProduct> dpsCellPolProductList = dpsPolService.getDpsCellPolProduct(ver, project);

        List<Map> mapList = new ArrayList<>();
        Map dps_pc_Map = new HashMap();
        for(DpsCellPolProduct dpsCellPolProduct : dpsCellPolProductList) {
            dps_pc_Map.put(dpsCellPolProduct.getFabDate().toString(), dpsCellPolProduct.getDemandQty());
        }
        mapList.add(dps_pc_Map);

        List<String> productList = bomPolService.getBomPolProduct(project);
        for(String product : productList) {
            Map subMap = new HashMap();
            subMap.put("project", project);
            subMap.put("product", product);
            List<DpsPol> dpsPolList = dpsPolService.getDpsPol(ver,product);
            for(DpsPol dpsPol : dpsPolList) {
                subMap.put(dpsPol.getFabDate().toString(), dpsPol.getDemandQty());

            }
            mapList.add(subMap);
        }
        return ResultUtil.successPage(mapList, mapList.size());
    }

    @PostMapping("/submitDpsSplitData")
    public Result submitDpsSplitData(String ver, String project, String jsonData) {
        List<Map> mapList;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            mapList = objectMapper.readValue(jsonData, new TypeReference<List<Map>>() {});
        } catch (IOException e) {
            return ResultUtil.error("提交数据解析错误");
        }

        //删除旧数据
        dpsPolService.delete(dpsPolService.getDpsPolByProject(ver, project));

        List<DpsPol> dpsPolList = new ArrayList<>();
        List<Date> dateList = dpsService.getDpsCalendar(ver);
        for(Map map : mapList) {
            String product = (String) map.get("product");
            if(product == null) continue;
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
                    DpsPol dpsPol = new DpsPol();
                    dpsPol.setVer(ver);
                    dpsPol.setFabDate(fabDate);
                    dpsPol.setDemandQty(demandQty);
                    dpsPol.setProduct(product);
                    dpsPol.setProject(project);
                    dpsPolList.add(dpsPol);
                }
            }

        }
        dpsPolService.save(dpsPolList);
        return ResultUtil.success("提交成功");
    }
}
