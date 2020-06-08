package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpMaterial;
import com.ivo.mrp2.repository.MrpMaterialRepository;
import com.ivo.mrp2.service.MrpMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class MrpMaterialServiceImpl implements MrpMaterialService {

    private MrpMaterialRepository mrpMaterialRepository;

    @Autowired
    public MrpMaterialServiceImpl(MrpMaterialRepository mrpMaterialRepository) {
        this.mrpMaterialRepository = mrpMaterialRepository;
    }

    @Override
    public MrpMaterial getMrpMaterial(String mrpVer, String material) {
        return mrpMaterialRepository.findByMrpVerAndMaterial(mrpVer, material);
    }

    @Override
    public List<String> getMaterial(String mrpVer) {
        return mrpMaterialRepository.getMaterial(mrpVer);
    }

    @Override
    public List<String> getMaterialGroup(String mrpVer) {
        return mrpMaterialRepository.getMaterialGroup(mrpVer);
    }

    @Override
    public void saveMrpMaterial(MrpMaterial mrpMaterial) {
        mrpMaterialRepository.save(mrpMaterial);
    }

    @Override
    public Page<MrpMaterial> getPageMrpData(int page, int limit, String mrpVer, String product, String materialGroup, String material) {
        if(material == null) material = "";
        if(product == null) product = "";
        if(materialGroup == null) materialGroup = "";
        material = material + "%";
        product = "%" + product + "%";
        materialGroup = materialGroup + "%";
        Pageable pageable = PageRequest.of(page-1, limit);
        return mrpMaterialRepository.getPage(mrpVer, product, materialGroup, material, pageable);
    }

    @Override
    public List<MrpMaterial> getMrpMaterial(String mrpVer) {
        return mrpMaterialRepository.findByMrpVer(mrpVer);
    }
}
