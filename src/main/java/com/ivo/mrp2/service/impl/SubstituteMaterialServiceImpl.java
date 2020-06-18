package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.SubstituteMaterial;
import com.ivo.mrp2.repository.SubstituteMaterialRepository;
import com.ivo.mrp2.service.BomService;
import com.ivo.mrp2.service.SubstituteMaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class SubstituteMaterialServiceImpl implements SubstituteMaterialService {

    private SubstituteMaterialRepository substituteMaterialRepository;

    private BomService bomService;

    @Autowired
    public SubstituteMaterialServiceImpl(SubstituteMaterialRepository substituteMaterialRepository, BomService bomService) {
        this.substituteMaterialRepository = substituteMaterialRepository;
        this.bomService = bomService;
    }

    @Override
    public Page<SubstituteMaterial> getPageSubstituteMaterial(int page, int limit, String plant, String product, String material, boolean effectFlag) {
        Pageable pageable = PageRequest.of(page, limit);
        return substituteMaterialRepository.findAll(pageable);
    }

    @Override
    public void abolish(Integer group) {
        List<SubstituteMaterial> substituteMaterialList = getSubstituteMaterialByGroup(group);
        for(SubstituteMaterial substituteMaterial : substituteMaterialList) {
            if(substituteMaterial.isEffectFlag()) {
                substituteMaterial.setExpireDate(new Date(System.currentTimeMillis()));
                substituteMaterial.setEffectFlag(false);
            }
        }
        substituteMaterialRepository.saveAll(substituteMaterialList);
    }

    @Override
    public void saveSubstituteMaterial(String plant, String product, List<Map> substituteMaterials) {
        log.info("保存材料替代组");
        List<SubstituteMaterial> substituteMaterialList = new ArrayList<>();
        for(Map map : substituteMaterials) {
            SubstituteMaterial substituteMaterial = new SubstituteMaterial();
            String material = (String) map.get("material");
            double substituteRate = Double.valueOf( (String) map.get("substituteRate") );
            if(getSubstituteMaterial(plant, product, material) != null) {
                throw new RuntimeException("材料"+material+"的替代关系已经维护");
            }
            if(substituteRate<=0 || substituteRate>=1) {
                throw new RuntimeException("材料"+material+"维护的替代比列范围要在0~1");
            }
            substituteMaterial.setPlant(plant);
            substituteMaterial.setProduct(product);
            substituteMaterial.setMaterial(material);
            substituteMaterial.setMaterialName(bomService.getMaterialName(material));
            substituteMaterial.setSubstituteGroup(substituteMaterialRepository.findAll().size()+1);
            substituteMaterial.setSubstituteRate(substituteRate);
            substituteMaterial.setEffectFlag(true);
            substituteMaterial.setEffectDate(new Date(System.currentTimeMillis()));
            substituteMaterialList.add(substituteMaterial);
        }
        substituteMaterialRepository.saveAll(substituteMaterialList);
    }

    @Override
    public double getMaterialSubstituteRate(String plant, String product, String material) {
        SubstituteMaterial substituteMaterial = getSubstituteMaterial(plant, product, material);
        if(substituteMaterial==null) {
            return 1;
        } else {
            return substituteMaterial.getSubstituteRate();
        }
    }

    @Override
    public SubstituteMaterial getSubstituteMaterial(String plant, String product, String material) {
        return substituteMaterialRepository.findByPlantAndProductAndMaterialAndEffectFlagIsTrue(plant, product, material);
    }

    @Override
    public List<SubstituteMaterial> getSubstituteMaterialByGroup(int group) {
        return substituteMaterialRepository.findBySubstituteGroup(group);
    }
}
