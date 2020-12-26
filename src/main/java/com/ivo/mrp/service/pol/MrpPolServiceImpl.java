package com.ivo.mrp.service.pol;

import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.entity.pol.MrpPol;
import com.ivo.mrp.entity.pol.MrpPolMaterial;
import com.ivo.mrp.key.MrpPolMaterialKey;
import com.ivo.mrp.repository.pol.MrpPolMaterialRepository;
import com.ivo.mrp.repository.pol.MrpPolRepository;
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
public class MrpPolServiceImpl implements MrpPolService {

    private MrpPolRepository mrpPolRepository;

    private MrpPolMaterialRepository mrpPolMaterialRepository;

    @Autowired
    public MrpPolServiceImpl(MrpPolRepository mrpPolRepository, MrpPolMaterialRepository mrpPolMaterialRepository) {
        this.mrpPolRepository = mrpPolRepository;
        this.mrpPolMaterialRepository = mrpPolMaterialRepository;
    }

    @Override
    public void saveMrpPol(List<MrpPol> list) {
        mrpPolRepository.saveAll(list);
    }

    @Override
    public void saveMrpPolMaterial(List<MrpPolMaterial> list) {
        mrpPolMaterialRepository.saveAll(list);
    }

    @Override
    public List<MrpPolMaterial> getMrpPolMaterial(String ver) {
        return mrpPolMaterialRepository.findByVer(ver);
    }

    @Override
    public List<MrpPol> getMrpPol(String ver, String product, String material) {
        return mrpPolRepository.findByVerAndProductAndMaterial(ver, product, material);
    }

    @Override
    public MrpPolMaterial getMrpPolMaterial(String ver, String product, String material) {
        MrpPolMaterialKey mrpPolMaterialKey = new MrpPolMaterialKey(ver, product, material);
        return mrpPolMaterialRepository.findById(mrpPolMaterialKey).orElse(null);
    }

    @Override
    public Page<MrpPolMaterial> getPageMrpPol(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "product", "supplierCode", "type");
        Specification<MrpPolMaterial> spec = (Specification<MrpPolMaterial>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("ver"), ver));
            if(StringUtils.isNotEmpty(searchProduct)) {
                predicates.add(criteriaBuilder.like(root.get("product"), "%"+searchProduct+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), searchMaterialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(searchMaterial)) {
                predicates.add(criteriaBuilder.like(root.get("material"), searchMaterial+"%"));
            }
            if(StringUtils.isNotEmpty(searchSupplier)) {
                predicates.add(criteriaBuilder.like(root.get("supplierName"), "%"+searchSupplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpPolMaterialRepository.findAll(spec, pageable);
    }
}
