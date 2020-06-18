package com.ivo.mrp2.controlller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.result.PageResult;
import com.ivo.common.result.Result;
import com.ivo.common.utils.ResultUtil;
import com.ivo.mrp2.entity.SubstituteMaterial;
import com.ivo.mrp2.service.SubstituteMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@RestController
@RequestMapping("/mrp/substituteMaterial")
public class SubstituteMaterialController {

    private SubstituteMaterialService substituteMaterialService;

    @Autowired
    public SubstituteMaterialController(SubstituteMaterialService substituteMaterialService) {
        this.substituteMaterialService = substituteMaterialService;
    }

    /**
     * 分页查询替代料数据
     * @param page 页数
     * @param limit 分页大小
     * @param plant 厂别
     * @param product  机种
     * @param material 料号
     * @param effectFlag 有效
     * @return PageResult
     */
    @RequestMapping("/getPageSubstituteMaterial")
    public PageResult getPageSubstituteMaterial(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "50") int limit,
                                            @RequestParam(defaultValue = "") String plant,
                                            @RequestParam(defaultValue = "") String product,
                                            @RequestParam(defaultValue = "") String material,
                                            @RequestParam(defaultValue = "true") boolean effectFlag) {
        Page p = substituteMaterialService.getPageSubstituteMaterial(page-1, limit, plant, product, material, effectFlag);
        return ResultUtil.successPage(p.getContent(), p.getTotalElements());
    }

    /**
     * 材料替代组提交
     * @param group 替代组
     * @param plant 厂别
     * @param product 机种
     * @param substituteMaterials 替代料
     * @return Result
     */
    @RequestMapping("/submit")
    public Result submit(@RequestParam(required = false) Integer group, @RequestParam String plant, @RequestParam String product,
                         @RequestParam String substituteMaterials) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map> materialList = new ArrayList<>();
        try {
            materialList = Arrays.asList(mapper.readValue(substituteMaterials, Map[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(materialList.size()==0) {
            return ResultUtil.error("没有添加替代哦料");
        }

        // 如果材料之前已维护过替代组，将之前的替代组失效
        if(group != null && group !=0) {
            substituteMaterialService.abolish(group);
        }

        substituteMaterialService.saveSubstituteMaterial(plant, product, materialList);
        return ResultUtil.success();
    }

    /**
     * 获取材料替代料
     * @param plant 厂别
     * @param product  机种
     * @param material 料号
     * @return PageResult
     */
    @RequestMapping("/getSubstituteMaterial")
    public Result getSubstituteMaterial( @RequestParam String plant, @RequestParam String product, @RequestParam String material) {
        SubstituteMaterial substituteMaterial = substituteMaterialService.getSubstituteMaterial(plant, product, material);
        List<SubstituteMaterial> substituteMaterialList = new ArrayList<>();
        if(substituteMaterial != null) {
            substituteMaterialList = substituteMaterialService.getSubstituteMaterialByGroup(substituteMaterial.getSubstituteGroup());
        }
        return ResultUtil.success(substituteMaterialList);
    }
}
