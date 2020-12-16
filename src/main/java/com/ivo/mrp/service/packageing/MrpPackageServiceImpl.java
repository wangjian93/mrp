package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.entity.packaging.MrpPackageMaterial;
import com.ivo.mrp.repository.packaging.MrpPackageMaterialRepository;
import com.ivo.mrp.repository.packaging.MrpPackageRepository;
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
public class MrpPackageServiceImpl implements MrpPackageService {

    private MrpPackageRepository mrpPackageRepository;

    private MrpPackageMaterialRepository mrpPackageMaterialRepository;

    @Autowired
    public MrpPackageServiceImpl(MrpPackageRepository mrpPackageRepository,
                                 MrpPackageMaterialRepository mrpPackageMaterialRepository) {
        this.mrpPackageRepository = mrpPackageRepository;
        this.mrpPackageMaterialRepository = mrpPackageMaterialRepository;
    }

    @Override
    public void saveMrpPackageMaterial(List<MrpPackageMaterial> list) {
        mrpPackageMaterialRepository.saveAll(list);
    }

    @Override
    public void saveMrpPackage(List<MrpPackage> list) {
        mrpPackageRepository.saveAll(list);
    }

    @Override
    public Page<MrpPackageMaterial> getPageMrpPackageMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpPackageMaterial> spec = (Specification<MrpPackageMaterial>) (root, query, criteriaBuilder) -> {
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
//            if(StringUtils.isNotEmpty(searchSupplier)) {
//                predicates.add(criteriaBuilder.like(root.get("suppliers"), "%"+searchSupplier+"%"));
//            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpPackageMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public List<MrpPackage> getMrpPackage(String ver, String packageId, String material) {
        return mrpPackageRepository.findByVerAndPackageIdAndMaterial(ver, packageId, material);
    }
}
