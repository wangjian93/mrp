package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.Material;
import com.ivo.mrp.entity.Project;
import com.ivo.mrp.repository.MaterialRepository;
import com.ivo.mrp.service.MaterialService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MaterialServiceImpl implements MaterialService {

    private MaterialRepository materialRepository;

    private RestService restService;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, RestService restService) {
        this.materialRepository = materialRepository;
        this.restService = restService;
    }

    private Material getMaterial(String material) {
        return materialRepository.findById(material).orElse(null);
    }

    @Override
    public void syncMaterial() {
        //从81数据库的表MM_O_Material同步数据
        log.info("物料数据同步>> START");
        List<Map> mapList = restService.getMaterial();
        if(mapList == null || mapList.size()==0) return;
        for(Map map : mapList) {
            Material material = new Material();
            material.setMaterial((String) map.get("material"));
            material.setMaterialName((String) map.get("materialName"));
            material.setMaterialGroup((String) map.get("materialGroup"));
            material.setMeasureUnit((String) map.get("measureUnit"));
            materialRepository.save(material);
        }
        log.info("物料数据同步>> END");
    }

    @Override
    public String getMaterialGroup(String material) {
        Material m = getMaterial(material);
        if(m != null)
            return m.getMaterialGroup();
        else
            log.warn("Material的表中没有料号"+material);
        return null;
    }

    @Override
    public String getMaterialName(String material) {
        Material m = getMaterial(material);
        if(m != null)
            return m.getMaterialName();
        else
            log.warn("Material的表中没有料号"+material);
        return null;
    }

    @Override
    public Page<Material> queryMaterial(int page, int limit, String search) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "material");
        return materialRepository.findByMaterialLikeOrMaterialNameLike(search+"%", search+"%", pageable);
    }

    @Override
    public Page<Material> queryMaterial(int page, int limit, String materialGroup, String search) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "material");
        return materialRepository.findByMaterialGroupLikeAndMaterialLike(materialGroup+"%", search+"%", pageable);
    }
}
