package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Material;
import com.ivo.mrp2.entity.MaterialGroup;
import com.ivo.mrp2.entity.MaterialSubstitute;
import com.ivo.mrp2.key.MaterialSubstituteKey;
import com.ivo.mrp2.repository.MaterialGroupRepository;
import com.ivo.mrp2.repository.MaterialRepository;
import com.ivo.mrp2.repository.MaterialSubstituteRepository;
import com.ivo.mrp2.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author wj
 * @version 1.0
 */
@Service
public class CacheServiceImpl implements CacheService {

    private MaterialRepository materialRepository;

    private MaterialGroupRepository materialGroupRepository;

    private MaterialSubstituteRepository materialSubstituteRepository;

    @Autowired
    public CacheServiceImpl(MaterialRepository materialRepository, MaterialGroupRepository materialGroupRepository,
                            MaterialSubstituteRepository materialSubstituteRepository) {
        this.materialRepository = materialRepository;
        this.materialGroupRepository = materialGroupRepository;
        this.materialSubstituteRepository = materialSubstituteRepository;
    }

    @Override
    @Cacheable(cacheNames = "materialGroup")
    public Material getMaterial(String material) {
        return materialRepository.findById(material).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "material")
    public MaterialGroup getMaterialGroup(String materialGroup) {
        return materialGroupRepository.findById(materialGroup).orElse(null);
    }

    @Override
    @Cacheable(cacheNames = "materialSubstitute")
    public MaterialSubstitute getMaterialSubstitute(String plant, String product, String material) {
        return materialSubstituteRepository.findById(new MaterialSubstituteKey(plant, product, material)).orElse(null);
    }
}
