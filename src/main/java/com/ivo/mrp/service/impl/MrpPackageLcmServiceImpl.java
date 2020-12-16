package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcm;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;
import com.ivo.mrp.repository.lcmPackage.MrpPackageLcmMaterialRepository;
import com.ivo.mrp.repository.lcmPackage.MrpPackageLcmRepository;
import com.ivo.mrp.service.MrpPackageLcmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class MrpPackageLcmServiceImpl implements MrpPackageLcmService {

    private MrpPackageLcmRepository mrpPackageLcmRepository;

    private MrpPackageLcmMaterialRepository mrpPackageLcmMaterialRepository;

    @Autowired
    public MrpPackageLcmServiceImpl(MrpPackageLcmRepository mrpPackageLcmRepository,
                                    MrpPackageLcmMaterialRepository mrpPackageLcmMaterialRepository) {
        this.mrpPackageLcmRepository = mrpPackageLcmRepository;
        this.mrpPackageLcmMaterialRepository = mrpPackageLcmMaterialRepository;
    }

    @Override
    public void saveMrpPackageLcm(List<MrpPackageLcm> list) {
        mrpPackageLcmRepository.saveAll(list);
    }

    @Override
    public void saveMrpPackageLcmMaterial(List<MrpPackageLcmMaterial> list) {
        mrpPackageLcmMaterialRepository.saveAll(list);
    }

    @Override
    public List<MrpPackageLcmMaterial> getMrpPackageLcmAloneMaterial(String ver) {
        return mrpPackageLcmMaterialRepository.findByVerAndIsAlone(ver, true);
    }

    @Override
    public List<MrpPackageLcmMaterial> getMrpPackageLcmProductMaterial(String ver) {
        return mrpPackageLcmMaterialRepository.findByVerAndIsAlone(ver, false);
    }

    @Override
    public List<MrpPackageLcm> getMrpPackageLcmAlone(String ver, String material) {
        return mrpPackageLcmRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpPackageLcm> getMrpPackageLcmProduct(String ver, String product, String material) {
        return mrpPackageLcmRepository.findByVerAndProductAndMaterialOrderByFabDateAsc(ver, product, material);
    }

    @Override
    public Page<MrpPackageLcmMaterial> getPageMrpPackageLcmMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "isAlone", "products", "materialGroup", "material");
        Specification<MrpPackageLcmMaterial> spec = (Specification<MrpPackageLcmMaterial>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("ver"), ver));
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("products"), "%"+searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            if(StringUtils.isNotEmpty(searchSupplier)) {
                predicates.add(criteriaBuilder.like(root.get("suppliers"), "%"+searchSupplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpPackageLcmMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public List<MrpPackageLcmMaterial> getMrpPackageLcmMaterial(String ver) {
        return mrpPackageLcmMaterialRepository.findByVer(ver);
    }
}
