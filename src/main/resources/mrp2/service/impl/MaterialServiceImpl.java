package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Material;
import com.ivo.mrp2.repository.MaterialRepository;
import com.ivo.mrp2.service.CacheService;
import com.ivo.mrp2.service.MaterialService;
import com.ivo.rest.eif.service.EifMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class MaterialServiceImpl implements MaterialService {

    private MaterialRepository materialRepository;


    private CacheService cacheService;

    private EifMaterialService eifMaterialService;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, CacheService cacheService,
                               EifMaterialService eifMaterialService) {
        this.materialRepository = materialRepository;
        this.cacheService = cacheService;
        this.eifMaterialService = eifMaterialService;
    }

    @Override
    public Material getMaterial(String material) {
        // 从缓存中获取数据
        return cacheService.getMaterial(material);
    }

    @Override
    public String getMaterialName(String material) {
        // 从缓存中获取数据
        Material obj = getMaterial(material);
        if(obj != null)
            return obj.getMaterialName();
        return null;
    }

    @Override
    public String getMaterialGroup(String material) {
        // 从缓存中获取数据
        Material obj = cacheService.getMaterial(material);
        if(obj != null)
            return obj.getMaterialGroup();
        return null;
    }

    @Override
    public void syncMaterial() {
        log.info("同步更新物料信息>> START");
        List<Material> materialList = new ArrayList<>();
        List<Map> mapList = eifMaterialService.getMaterial();
        if(mapList==null || mapList.size()==0) return;
        for(Map map : mapList) {
            Material material = new Material();
            material.setMaterial( ((String)map.get("material")).trim());
            material.setMaterialName( ((String)map.get("materialName")).trim());
            material.setMaterialGroup( ((String)map.get("materialGroup")).trim());
            material.setMeasureUnit( ((String)map.get("measureUnit")).trim());
            material.setMemo("来自EIF同步");
            materialList.add(material);
        }
        materialRepository.saveAll(materialList);
        log.info("同步更新物料信息>> END");
    }

    @Override
    public List<String> searchMaterial(String materialSearch, int limit) {
        return materialRepository.searchMaterial(materialSearch+"%", limit);
    }

    @Override
    public List<String> getMaterialByGroup(String materialGroup) {
        return materialRepository.findMaterialByMaterialGroup(materialGroup);
    }
}
