package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpMaterial;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.service.MrpDataService;
import com.ivo.mrp2.service.MrpMaterialService;
import com.ivo.mrp2.service.MrpService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缺料分配
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/allocation")
public class AllocationController {

    private MrpMaterialService mrpMaterialService;
    private MrpService2 mrpService;
    private MrpDataService mrpDataService;

    @Autowired
    public AllocationController(MrpMaterialService mrpMaterialService, MrpService2 mrpService, MrpDataService mrpDataService) {
        this.mrpMaterialService = mrpMaterialService;
        this.mrpService = mrpService;
        this.mrpDataService = mrpDataService;
    }

    /**
     * 获取缺料分配列表
     * @param mrpVer MRP版本
     * @return PageResult
     */
    @RequestMapping("/list")
    public PageResult list(@RequestParam String mrpVer) {
        MrpVer mrp = mrpService.getMrpVer(mrpVer);
        List<MrpMaterial> mrpMaterialList = mrpMaterialService.getMrpMaterial(mrpVer);
        List<Map> mapList = new ArrayList<>();
        Map<String, Integer> cursor = new HashMap<>();   //游标
        for(MrpMaterial mrpMaterial : mrpMaterialList) {
            Map<String, Object> map = new HashMap<>();
            map.put("plant", mrp.getPlant());
            map.put("material", mrpMaterial.getMaterial());
            map.put("materialName", mrpMaterial.getMaterialName());
            map.put("products", mrpMaterial.getProducts());
            map.put("materialGroup", mrpMaterial.getMaterialGroup());
            mapList.add(map);
            cursor.put(mrpMaterial.getMaterial(), mapList.size()-1);
        }

        List<MrpData> mrpDataList = mrpDataService.getShortMrpData(mrpVer);
        if(mrpDataList != null && mrpDataList.size()>0) {
            for(MrpData mrpData : mrpDataList) {
                Map<String, Object> map = mapList.get(cursor.get(mrpData.getMaterial()));
                map.put(mrpData.getFabDate().toString(), mrpData.getShortQty());
            }
        }
        return ResultUtil.successPage(mapList, mapList.size());
    }
}
