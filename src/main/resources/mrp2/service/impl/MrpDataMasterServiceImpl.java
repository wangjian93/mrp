package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.MrpDataMaster;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.repository.MrpDataMasterRepository;
import com.ivo.mrp2.service.MrpDataMasterService;
import com.ivo.mrp2.service.MrpVerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class MrpDataMasterServiceImpl implements MrpDataMasterService {

    private MrpDataMasterRepository mrpDataMasterRepository;

    private MrpVerService mrpVerService;

    @Autowired
    public MrpDataMasterServiceImpl(MrpDataMasterRepository mrpDataMasterRepository, MrpVerService mrpVerService) {
        this.mrpDataMasterRepository = mrpDataMasterRepository;
        this.mrpVerService = mrpVerService;
    }

    @Override
    public MrpDataMaster getMrpMaterial(String mrpVer, String material) {
        return mrpDataMasterRepository.findByMrpVerAndMaterial(mrpVer, material);
    }

    @Override
    public List<String> getMaterial(String mrpVer) {
        return mrpDataMasterRepository.getMaterial(mrpVer, "%");
    }

    @Override
    public List<String> getMaterial(String mrpVer, String searchMaterial) {
        return mrpDataMasterRepository.getMaterial(mrpVer, searchMaterial+"%");
    }

    @Override
    public List<String> getMaterialGroup(String mrpVer, String searchMaterialGroup) {
        return mrpDataMasterRepository.getMaterialGroup(mrpVer, searchMaterialGroup+"%");
    }

    @Override
    public List<String> getProduct(String mrpVer, String searchProduct) {
        MrpVer m = mrpVerService.getMrpVer(mrpVer);
        String dpsVers = m.getDpsVer();
        String[] dpsVers_ = dpsVers.split(",");
        List<String> dpsVerList = Arrays.asList(dpsVers_);
        return mrpDataMasterRepository.getProduct(dpsVerList, searchProduct+"%");
    }

    @Override
    public List<String> getSupplier(String mrpVer, String searchSupplier) {
        return mrpDataMasterRepository.getSupplier(mrpVer, searchSupplier+"%");
    }

    @Override
    public List<String> getSupplier(String mrpVer) {
        return null;
    }

    @Override
    public void saveMrpMaterial(MrpDataMaster mrpDataMaster) {
        mrpDataMasterRepository.save(mrpDataMaster);
    }

    @Override
    public List<MrpDataMaster> getMrpMaterial(String mrpVer) {
        return mrpDataMasterRepository.findByMrpVer(mrpVer);
    }




    @Override
    public Page<MrpDataMaster> getPageMrpData(int page, int limit, String mrpVer, String product, String materialGroup, String material, String supplier) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "materialGroup");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "material");
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        Sort sort = Sort.by(orderList);
        Pageable pageable = PageRequest.of(page, limit, sort);
        //封装查询对象Specification  这是个自带的动态条件查询
        Specification<MrpDataMaster> spec = (Specification<MrpDataMaster>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("mrpVer"), mrpVer));
            if(StringUtils.isNotEmpty(product)) {
                predicates.add(criteriaBuilder.like(root.get("products"), "%"+product+"%"));
            }
            if(StringUtils.isNotEmpty(materialGroup)) {
                predicates.add(criteriaBuilder.equal(root.get("materialGroup"), materialGroup));
            }
            if(StringUtils.isNotEmpty(material)) {
                predicates.add(criteriaBuilder.equal(root.get("material"), material));
            }
            if(StringUtils.isNotEmpty(supplier)) {
                predicates.add(criteriaBuilder.like(root.get("supplier"), "%"+supplier+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpDataMasterRepository.findAll(spec, pageable);
    }

    @Override
    public Page<String> getPageMrpData(int page, int limit, List<String> mrpVers, String product, String materialGroup, String material) {
        Sort sort = Sort.by(Sort.Direction.ASC, "material");
        Pageable pageable = PageRequest.of(page, limit, sort);
        return mrpDataMasterRepository.getPage(mrpVers, product+"%", materialGroup+"%", material+"%", pageable);
    }
}
