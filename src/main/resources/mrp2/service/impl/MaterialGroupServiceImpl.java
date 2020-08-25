package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MaterialGroup;
import com.ivo.mrp2.repository.MaterialGroupRepository;
import com.ivo.mrp2.service.CacheService;
import com.ivo.mrp2.service.MaterialGroupService;
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
public class MaterialGroupServiceImpl implements MaterialGroupService {

    private MaterialGroupRepository materialGroupRepository;

    private CacheService cacheService;

    private EifMaterialService eifMaterialService;

    @Autowired
    public MaterialGroupServiceImpl(MaterialGroupRepository materialGroupRepository,
                                    CacheService cacheService, EifMaterialService eifMaterialService) {
        this.materialGroupRepository = materialGroupRepository;
        this.cacheService = cacheService;
        this.eifMaterialService = eifMaterialService;
    }

    @Override
    public String getMaterialGroupName(String materialGroup) {
        MaterialGroup obj = cacheService.getMaterialGroup(materialGroup);
        if(obj != null)
            return obj.getMaterialGroupName();
        return null;
    }

    @Override
    public void syncMaterialGroup() {
        log.info("同步更新物料组信息>> START");
        List<MaterialGroup> materialGroupList = new ArrayList<>();
        List<Map> mapList = eifMaterialService.getMaterialGroup();
        if(mapList==null || mapList.size()==0) return;
        for(Map map : mapList) {
            MaterialGroup materialGroup = new MaterialGroup();
            materialGroup.setMaterialGroup( ((String)map.get("materialGroup")).trim());
            materialGroup.setMaterialGroupName( ((String)map.get("materialGroupName")).trim());
            materialGroup.setMemo("来自EIF同步");
            materialGroupList.add(materialGroup);
        }
        materialGroupRepository.saveAll(materialGroupList);
        log.info("同步更新物料组信息>> END");
    }

    @Override
    public List<String> searchMaterialGroup(String materialGroupSearch, int limit) {
        return materialGroupRepository.searchMaterialGroup(materialGroupSearch+"%", limit);
    }
}
