package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.MaterialGroup;
import com.ivo.mrp.entity.Project;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.repository.MaterialGroupRepository;
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
public class MaterialGroupServiceImpl implements MaterialGroupService {

    private MaterialGroupRepository materialGroupRepository;

    private RestService restService;

    @Autowired
    public MaterialGroupServiceImpl(MaterialGroupRepository materialGroupRepository, RestService restService) {
        this.materialGroupRepository = materialGroupRepository;
        this.restService = restService;
    }

    private MaterialGroup getMaterialGroup(String materialGroup) {
        return materialGroupRepository.findById(materialGroup).orElse(null);
    }

    @Override
    public void syncMaterialGroup() {
        //从81数据库的表MM_O_MaterialGroup同步数据
        log.info("物料组数据同步>> START");
        List<Map> mapList = restService.getMaterialGroup();
        if(mapList == null || mapList.size()==0) return;
        for(Map map : mapList) {
            MaterialGroup materialGroup = new MaterialGroup();
            materialGroup.setMaterialGroup((String) map.get("materialGroup"));
            materialGroup.setMaterialGroupName((String) map.get("materialGroupName"));
            materialGroupRepository.save(materialGroup);
        }
        log.info("物料组数据同步>> END");
    }

    @Override
    public String getMaterialGroupName(String materialGroup) {
        MaterialGroup m = getMaterialGroup(materialGroup);
        if(m != null)
            return m.getMaterialGroupName();
        else
            log.warn("MaterialGroup表中没有物料组" + materialGroup);
        return null;
    }

    @Override
    public Page<MaterialGroup> queryMaterialGroup(int page, int limit, String search) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup");
        return materialGroupRepository.findByMaterialGroupLikeOrMaterialGroupNameLike(search+"%", search+"%", pageable);
    }
}
