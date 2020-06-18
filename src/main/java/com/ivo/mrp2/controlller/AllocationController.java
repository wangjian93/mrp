package com.ivo.mrp2.controlller;

import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.MaterialSupplier;
import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpMaterial;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.service.MaterialSupplierService;
import com.ivo.mrp2.service.MrpDataService;
import com.ivo.mrp2.service.MrpMaterialService;
import com.ivo.mrp2.service.MrpService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    private MaterialSupplierService materialSupplierService;

    @Autowired
    public AllocationController(MrpMaterialService mrpMaterialService, MrpService2 mrpService, MrpDataService mrpDataService,
                                MaterialSupplierService materialSupplierService) {
        this.mrpMaterialService = mrpMaterialService;
        this.mrpService = mrpService;
        this.mrpDataService = mrpDataService;
        this.materialSupplierService = materialSupplierService;
    }

    /**
     * 获取缺料分配列表
     * @param mrpVer MRP版本
     * @return PageResult
     */
    @RequestMapping("/list")
    public PageResult list(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                           @RequestParam String mrpVer) {
        MrpVer mrp = mrpService.getMrpVer(mrpVer);
        Page<MrpMaterial> materialPage = mrpMaterialService.getPageMrpData(page, limit, mrpVer, "", "", "");
        List<MrpMaterial> mrpMaterialList = materialPage.getContent();
        List<Map> mapList = new ArrayList<>();
        List<String> materialList = new ArrayList<>();
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
            materialList.add(mrpMaterial.getMaterial());
        }

        List<MrpData> mrpDataList = mrpDataService.getShortMrpData(mrpVer, materialList);
        if(mrpDataList != null && mrpDataList.size()>0) {
            for(MrpData mrpData : mrpDataList) {
                Map<String, Object> map = mapList.get(cursor.get(mrpData.getMaterial()));
                Map<String, Double> subMap = new HashMap<>();
                subMap.put("demandQty", mrpData.getDemandQty());
                subMap.put("lossQty", mrpData.getLossQty());
                subMap.put("arrivalQty", mrpData.getArrivalQty());
                subMap.put("balanceQty", mrpData.getBalanceQty());
                subMap.put("shortQt", mrpData.getShortQty());
                map.put(mrpData.getFabDate().toString(), subMap);
            }
        }
        return ResultUtil.successPage(mapList, materialPage.getTotalElements());
    }

    /**
     * 获取供应商
     * @param material 料号
     * @return Result
     */
    @RequestMapping("/getSupplier")
    public Result getSupplier(String material) {
        List<MaterialSupplier> list = materialSupplierService.getMaterialSupplier(material);
        return ResultUtil.success(list);
    }
}
