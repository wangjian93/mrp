package com.ivo.mrp2.service.impl;

import com.ivo.common.BatchService;
import com.ivo.mrp2.entity.MaterialSubstitute;
import com.ivo.mrp2.repository.MaterialSubstituteRepository;
import com.ivo.mrp2.service.*;
import com.ivo.rest.eif.service.EifBomService;
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
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MaterialSubstituteServiceImpl implements MaterialSubstituteService {

    private MaterialSubstituteRepository materialSubstituteRepository;

    private CacheService cacheService;

    private EifBomService eifBomService;

    private MaterialService materialService;

    private MaterialGroupService materialGroupService;

    private BatchService batchService;

    @Autowired
    public MaterialSubstituteServiceImpl(MaterialSubstituteRepository materialSubstituteRepository,
                                         CacheService cacheService, EifBomService eifBomService,
                                         MaterialService materialService, MaterialGroupService materialGroupService,
                                         BatchService batchService) {
        this.materialSubstituteRepository = materialSubstituteRepository;
        this.cacheService = cacheService;
        this.eifBomService = eifBomService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.batchService = batchService;
    }

//    @Override
//    public void abolish(Integer group) {
//        List<MaterialSubstitute> materialSubstituteList = getSubstituteMaterialByGroup(group);
//        for(MaterialSubstitute materialSubstitute : materialSubstituteList) {
//            if(materialSubstitute.isEffectFlag()) {
//                materialSubstitute.setExpireDate(new Date(System.currentTimeMillis()));
//                materialSubstitute.setEffectFlag(false);
//            }
//        }
//        materialSubstituteRepository.saveAll(materialSubstituteList);
//    }
//
//    @Override
//    public void saveSubstituteMaterial(String plant, String product, List<Map> substituteMaterials) {
//        log.info("保存材料替代组");
//        List<MaterialSubstitute> materialSubstituteList = new ArrayList<>();
//        for(Map map : substituteMaterials) {
//            MaterialSubstitute materialSubstitute = new MaterialSubstitute();
//            String material = (String) map.get("material");
//            double substituteRate = Double.valueOf( (String) map.get("substituteRate") );
//            if(getSubstituteMaterial(plant, product, material) != null) {
//                throw new RuntimeException("材料"+material+"的替代关系已经维护");
//            }
//            if(substituteRate<0 || substituteRate>1) {
//                throw new RuntimeException("材料"+material+"维护的替代比列范围要在0~1");
//            }
//            materialSubstitute.setPlant(plant);
//            materialSubstitute.setProduct(product);
//            materialSubstitute.setMaterial(material);
//            materialSubstitute.setMaterialName(materialService.getMaterialName(material));
////            materialSubstitute.setSubstituteGroup(materialSubstituteRepository.findAll().size()+1);
//            materialSubstitute.setSubstituteRate(substituteRate);
//            materialSubstitute.setEffectFlag(true);
//            materialSubstitute.setEffectDate(new Date(System.currentTimeMillis()));
//            materialSubstituteList.add(materialSubstitute);
//        }
//        materialSubstituteRepository.saveAll(materialSubstituteList);
//    }
//
//
//
//    @Override
//    public MaterialSubstitute getSubstituteMaterial(String plant, String product, String material) {
//        return materialSubstituteRepository.findByPlantAndProductAndMaterialAndEffectFlagIsTrue(plant, product, material);
//    }
//
//    @Override
//    public List<MaterialSubstitute> getSubstituteMaterialByGroup(int group) {
////        return materialSubstituteRepository.findBySubstituteGroup(group);
//        return new ArrayList<>();
//    }
//
//
//
//
//
//
//
//
//    @Override
//    public List<MaterialSubstitute> getMaterialSubstituteGroup(String plant, String product, String materialGroup) {
//        return materialSubstituteRepository.findByEffectFlagIsTrueAndPlantAndProductAndMaterialGroup(plant, product, materialGroup);
//    }
//
//    @Override
//    public void addMaterialSubstitute(String plant, String product, String materialGroup, Map<String, Double> substituteRateMap) {
//        log.info("添加一组替代料>> START");
//        abolishMaterialSubstitute(plant, product, materialGroup);
//        List<MaterialSubstitute> materialSubstituteList = new ArrayList<>();
//        // 替代比例总和需要为1
//        double sum = 0;
//        for(String material : substituteRateMap.keySet()) {
//            MaterialSubstitute materialSubstitute = new MaterialSubstitute();
//            materialSubstitute.setPlant(plant);
//            materialSubstitute.setProduct(product);
//            materialSubstitute.setMaterialGroup(materialGroup);
//            materialSubstitute.setMaterial(material);
//            Double substituteRate = substituteRateMap.get(material);
//            materialSubstitute.setSubstituteRate(substituteRate);
//            sum = DoubleUtil.sum(sum, substituteRate);
//            materialSubstitute.setMaterialName(materialService.getMaterialName(material));
//            materialSubstitute.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
//            materialSubstitute.setEffectFlag(true);
//            materialSubstitute.setEffectDate(new java.util.Date());
//            materialSubstitute.setMemo("MC维护");
//
//            materialSubstituteList.add(materialSubstitute);
//        }
//        if(sum != 100) {
//            throw new RuntimeException("维护的替代比列不正确");
//        }
//        materialSubstituteRepository.saveAll(materialSubstituteList);
//        log.info("添加一组替代料>> END");
//    }
//
//    @Override
//    public void abolishMaterialSubstitute(String plant, String product, String materialGroup) {
//        log.info("废止一组替代料>> START");
//        List<MaterialSubstitute> materialSubstituteList = getMaterialSubstituteGroup(plant, product, materialGroup);
//        for(MaterialSubstitute materialSubstitute : materialSubstituteList) {
//            materialSubstitute.setEffectFlag(false);
//            materialSubstitute.setExpireDate(new java.util.Date());
//        }
//        materialSubstituteRepository.saveAll(materialSubstituteList);
//        log.info("废止一组替代料>> END");
//    }
//
//    @Override
//    public Page<MaterialSubstitute> getPageMaterialSubstitute(int page, int limit, String plant, String product, String materialGroup, boolean effectFlag) {
//        Pageable pageable = PageRequest.of(page, limit);
//        //封装查询对象Specification  这是个自带的动态条件查询
//        Specification<MaterialSubstitute> spec = (Specification<MaterialSubstitute>) (root, query, criteriaBuilder) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if(StringUtils.isNotEmpty(plant)) {
//                predicates.add(criteriaBuilder.like(root.get("plant"), "%"+plant+"%"));
//            }
//            if(StringUtils.isNotEmpty(product)) {
//                predicates.add(criteriaBuilder.like(root.get("product"), "%"+product+"%"));
//            }
//            if(StringUtils.isNotEmpty(materialGroup)) {
//                predicates.add(criteriaBuilder.like(root.get("materialGroup"), "%"+materialGroup+"%"));
//            }
//            predicates.add(criteriaBuilder.equal(root.get("effectFlag"), effectFlag));
//            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
//        };
//        return materialSubstituteRepository.findAll(spec, pageable);
//    }





    @Override
    public void syncMaterialSubstitute() {
        log.info("同步更新替代料>> START");
        materialSubstituteRepository.truncate();
        List<Map> mapListLcm1 = eifBomService.getMaterialSubstituteLcm1();
        List<Map> mapListLcm2 = eifBomService.getMaterialSubstituteLcm2();
        List<Map> mapListCell = eifBomService.getMaterialSubstituteCell();

        List<Map> mapList = new ArrayList<>();
        mapList.addAll(mapListLcm1);
        mapList.addAll(mapListLcm2);
        mapList.addAll(mapListCell);
        List<MaterialSubstitute> materialSubstituteList = new ArrayList<>();
        for(Map map : mapList) {
            String plant = ((String) map.get("plant")).trim();
            String product = ((String) map.get("product")).trim();
            String materialGroup = ((String) map.get("materialGroup")).trim();
            String material = ((String) map.get("material")).trim();
            double rate;
            if( map.get("rate") == null) {
                rate = 100;
            } else {
                rate = (double) map.get("rate");
            }
            MaterialSubstitute materialSubstitute = new MaterialSubstitute();
            materialSubstitute.setPlant(plant);
            materialSubstitute.setProduct(product);
            materialSubstitute.setMaterial(material);
            materialSubstitute.setMaterialGroup(materialGroup);
            materialSubstitute.setSubstituteRate(rate);
            materialSubstitute.setMaterialName(materialService.getMaterialName(material));
            materialSubstitute.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
            materialSubstitute.setMemo("BOM更新维护");
            materialSubstituteList.add(materialSubstitute);
        }
        save(materialSubstituteList);
        log.info("同步更新替代料>> END");
    }

    @Override
    public void save(List<MaterialSubstitute> materialSubstituteList) {
        log.info("保存替代料>> START");
        materialSubstituteRepository.saveAll(materialSubstituteList);
        log.info("保存替代料>> END");
    }

    @Override
    public double getMaterialSubstituteRate(String plant, String product, String material) {
        MaterialSubstitute materialSubstitute = cacheService.getMaterialSubstitute(plant, product, material);
        if(materialSubstitute ==null) {
            return 100;
        } else {
            return materialSubstitute.getSubstituteRate();
        }
    }

    @Override
    public Page<MaterialSubstitute> getMaterialSubstitute(int page, int limit, String plant, String product, String materialGroup) {
        Sort.Order order1 = new Sort.Order(Sort.Direction.ASC, "plant");
        Sort.Order order2 = new Sort.Order(Sort.Direction.ASC, "product");
        Sort.Order order3 = new Sort.Order(Sort.Direction.ASC, "materialGroup");
        Sort.Order order4 = new Sort.Order(Sort.Direction.ASC, "material");
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);
        Sort sort = Sort.by(orderList);
        Pageable pageable = PageRequest.of(page, limit, sort);
        //封装查询对象Specification  这是个自带的动态条件查询
        Specification<MaterialSubstitute> spec = (Specification<MaterialSubstitute>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(plant)) {
                predicates.add(criteriaBuilder.equal(root.get("plant"), plant));
            }
            if(StringUtils.isNotEmpty(product)) {
                predicates.add(criteriaBuilder.like(root.get("product"), product+"%"));
            }
            if(StringUtils.isNotEmpty(materialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), materialGroup+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return materialSubstituteRepository.findAll(spec, pageable);
    }

    @Override
    public List<MaterialSubstitute> getMaterialSubstitute(String plant, String product, String materialGroup) {
        return materialSubstituteRepository.findByPlantAndProductAndMaterialGroup(plant, product, materialGroup);
    }

    @Override
    public void saveMaterialSubstitute(String plant, String product, String materialGroup, Map<String, Double> materialSubstituteMap) {
        if(materialSubstituteMap == null || materialSubstituteMap.size()==0) return;
        log.info("保存机种"+product+"的替代料>> START");
        // 材料替代比例总和要等于100
        double totalRate = 0;
        List<MaterialSubstitute> newMsList = new ArrayList<>();
        for(String material : materialSubstituteMap.keySet()) {
            double substituteRate = materialSubstituteMap.get(material);
            totalRate += substituteRate;
            MaterialSubstitute materialSubstitute = new MaterialSubstitute();
            materialSubstitute.setPlant(plant);
            materialSubstitute.setProduct(product);
            materialSubstitute.setMaterialGroup(materialGroup);
            materialSubstitute.setMaterial(material);
            materialSubstitute.setSubstituteRate(substituteRate);
            materialSubstitute.setMaterialName(materialService.getMaterialName(material));
            materialSubstitute.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
            materialSubstitute.setMemo("MC维护");
            newMsList.add(materialSubstitute);
        }
        if(totalRate != 100) {
           throw new RuntimeException("材料替代比例总和不等于100");
        }
        materialSubstituteRepository.deleteAll(getMaterialSubstitute(plant, product, materialGroup));
        materialSubstituteRepository.saveAll(newMsList);
        log.info("保存替代料>> END");
    }
}
