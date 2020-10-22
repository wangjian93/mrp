package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.Substitute;
import com.ivo.mrp.entity.direct.ary.MrpAry;
import com.ivo.mrp.entity.direct.ary.MrpAryMaterial;
import com.ivo.mrp.entity.direct.cell.MrpCell;
import com.ivo.mrp.entity.direct.cell.MrpCellMaterial;
import com.ivo.mrp.entity.direct.lcm.MrpLcm;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.key.MrpKey;
import com.ivo.mrp.key.MrpMaterialKey;
import com.ivo.mrp.repository.*;
import com.ivo.mrp.service.MrpService;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MrpServiceImpl implements MrpService {

    private MrpVerRepository mrpVerRepository;

    private MrpLcmMaterialRepository mrpLcmMaterialRepository;

    private MrpAryMaterialRepository mrpAryMaterialRepository;

    private MrpCellMaterialRepository mrpCellMaterialRepository;

    private MrpLcmRepository mrpLcmRepository;

    private MrpAryRepository mrpAryRepository;

    private MrpCellRepository mrpCellRepository;

    private MrpPackageRpository mrpPackageRpository;

    @Autowired
    public MrpServiceImpl(MrpVerRepository mrpVerRepository, MrpLcmMaterialRepository mrpLcmMaterialRepository,
                          MrpAryMaterialRepository mrpAryMaterialRepository, MrpCellMaterialRepository mrpCellMaterialRepository,
                          MrpLcmRepository mrpLcmRepository, MrpAryRepository mrpAryRepository,
                          MrpCellRepository mrpCellRepository,
                          MrpPackageRpository mrpPackageRpository) {
        this.mrpVerRepository = mrpVerRepository;
        this.mrpLcmMaterialRepository = mrpLcmMaterialRepository;
        this.mrpAryMaterialRepository = mrpAryMaterialRepository;
        this.mrpCellMaterialRepository = mrpCellMaterialRepository;
        this.mrpLcmRepository = mrpLcmRepository;
        this.mrpAryRepository = mrpAryRepository;
        this.mrpCellRepository = mrpCellRepository;
        this.mrpPackageRpository = mrpPackageRpository;
    }

    @Override
    public void saveMrpVer(MrpVer mrpVer) {
        mrpVerRepository.save(mrpVer);
    }

    @Override
    public long countMrp() {
        return mrpVerRepository.count();
    }

    @Override
    public MrpVer getMrpVer(String ver) {
        return mrpVerRepository.findById(ver).orElse(null);
    }

    @Override
    public void delMrpVer(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        mrpVer.setValidFlag(false);
        mrpVer.setUpdateDate(new java.util.Date());
        saveMrpVer(mrpVer);
    }

    @Override
    public String convertAryToString(String[] vers) {
        if(vers == null || vers.length == 0) {
            return null;
        }
        StringBuilder verStr = null;
        for(String ver : vers) {
            if(verStr == null) {
                verStr = new StringBuilder(ver);
            } else {
                verStr.append(",").append(ver);
            }
        }
        return verStr.toString();
    }

    @Override
    public String[] convertStringToAry(String ver) {
        if(ver == null || ver.equals("")) return new String[0];
        return ver.split(",");
    }

    @Override
    public void saveMrpLcmMaterial(List<MrpLcmMaterial> list) {
        mrpLcmMaterialRepository.saveAll(list);
    }

    @Override
    public void saveMrpAryMaterial(List<MrpAryMaterial> list) {
        mrpAryMaterialRepository.saveAll(list);
    }

    @Override
    public void saveMrpCellMaterial(List<MrpCellMaterial> list) {
        mrpCellMaterialRepository.saveAll(list);
    }

    @Override
    public List<MrpLcmMaterial> getMrpLcmMaterial(String ver) {
        return mrpLcmMaterialRepository.findByVer(ver);
    }

    @Override
    public List<MrpAryMaterial> getMrpAryMaterial(String ver) {
        return mrpAryMaterialRepository.findByVer(ver);
    }

    @Override
    public List<MrpCellMaterial> getMrpCellMaterial(String ver) {
        return mrpCellMaterialRepository.findByVer(ver);
    }

    @Override
    public List<String> getMaterialLcm(String ver) {
        return mrpLcmMaterialRepository.getMaterial(ver);
    }

    @Override
    public List<String> getMaterialAry(String ver) {
        return mrpAryMaterialRepository.getMaterial(ver);
    }

    @Override
    public List<String> getMaterialCell(String ver) {
        return mrpCellMaterialRepository.getMaterial(ver);
    }

    @Override
    public MrpLcmMaterial getMrpLcmMaterial(String ver, String material) {
        return mrpLcmMaterialRepository.findById(new MrpMaterialKey(ver, material)).orElse(null);
    }

    @Override
    public MrpAryMaterial getMrpAryMaterial(String ver, String material) {
        return mrpAryMaterialRepository.findById(new MrpMaterialKey(ver, material)).orElse(null);
    }

    @Override
    public MrpCellMaterial getMrpCellMaterial(String ver, String material) {
        return mrpCellMaterialRepository.findById(new MrpMaterialKey(ver, material)).orElse(null);
    }

    @Override
    public List<MrpLcm> getMrpLcm(String ver, String material) {
        return mrpLcmRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpAry> getMrpAry(String ver, String material) {
        return mrpAryRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpCell> getMrpCell(String ver, String material) {
        return mrpCellRepository.findByVerAndMaterialOrderByFabDateAsc(ver, material);
    }

    @Override
    public List<MrpCell> getMrpCell(String ver) {
        return mrpCellRepository.findByVerOrderByFabDateAsc(ver);
    }

    @Override
    public List<MrpLcm> getMrpLcm(String ver) {
        return mrpLcmRepository.findByVerOrderByMaterialAsc(ver);
    }

    @Override
    public List<MrpAry> getMrpAry(String ver) {
        return mrpAryRepository.findByVerOrderByFabDateAsc(ver);
    }

    @Override
    public List<MrpPackage> getMrpPackage(String ver) {
        Sort sort = new Sort(Sort.Direction.ASC, "product", "type", "linkQty", "mode", "material");
        return mrpPackageRpository.findByVer(ver, sort);
    }

    @Override
    public List<Date> getMrpCalendar(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        if(mrpVer == null) return new ArrayList<>();
        List<java.util.Date> dateList = DateUtil.getCalendar(mrpVer.getStartDate(), mrpVer.getEndDate());
        List<Date> dates = new ArrayList<>();
        for(java.util.Date d : dateList) {
            dates.add(new Date(d.getTime()));
        }
        return dates;
    }

    @Override
    public void saveMrpLcm(List<MrpLcm> list) {
        mrpLcmRepository.saveAll(list);
    }

    @Override
    public void saveMrpAry(List<MrpAry> list) {
        mrpAryRepository.saveAll(list);
    }

    @Override
    public void saveMrpCell(List<MrpCell> list) {
        mrpCellRepository.saveAll(list);
    }

    @Override
    public List<MrpPackage> getMrpPackage(String ver, String product, String type, Double linkQty, String mode) {
        return mrpPackageRpository.findByVerAndProductAndTypeAndLinkQtyAndMode(ver, product, type, linkQty, mode);
    }

    @Override
    public void saveMrpPackage(List<MrpPackage> list) {
        mrpPackageRpository.saveAll(list);
    }

    @Override
    public void deleteMrpPackage(List<MrpPackage> list) {
        mrpPackageRpository.deleteAll(list);
    }

    @Override
    public Page<MrpVer> queryMrpVer(int page, int limit, String searchFab, String searchType, String searchVer) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "ver");
        return mrpVerRepository.findByFabLikeAndTypeLikeAndVerLikeAndValidFlagIsTrue(searchFab+"%", searchType+"%", searchVer+"%", pageable);
    }

    @Override
    public List getMrpDate(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        if(mrpVer == null) return new ArrayList();
        String type = mrpVer.getType();
        List list;
        switch (type) {
            case MrpVer.Type_Ary:
                list = getMrpAry(ver);
                break;
            case MrpVer.Type_Cell:
                list = getMrpCell(ver);
                break;
            case MrpVer.Type_Lcm:
                list = getMrpLcm(ver);
                break;
            case MrpVer.Type_Package:
                list = getMrpPackage(ver);
                break;
            default:
                list = new ArrayList();
        }
        return list;
    }

    @Override
    public Page<MrpLcmMaterial> getPageMrpLcmMaterial(int page, int limit, String ver, String searchProduct,
                                                      String searchMaterialGroup, String searchMaterial,
                                                      String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpLcmMaterial> spec = (Specification<MrpLcmMaterial>) (root, query, criteriaBuilder) -> {
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
        return mrpLcmMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public List<MrpLcm> getMrpLcm(String ver, List<String> materialList) {
        return mrpLcmRepository.findByVerAndMaterialInOrderByMaterialAsc(ver, materialList);
    }

    @Override
    public MrpLcm getMrpLcm(String ver, String material, Date fabDate) {
        return mrpLcmRepository.findById(new MrpKey(ver, fabDate, material)).orElse(null);
    }

    @Override
    public MrpCell getMrpCell(String ver, String material, Date fabDate) {
        return mrpCellRepository.findById(new MrpKey(ver, fabDate, material)).orElse(null);
    }

    @Override
    public MrpAry getMrpAry(String ver, String material, Date fabDate) {
        return mrpAryRepository.findById(new MrpKey(ver, fabDate, material)).orElse(null);
    }

    @Override
    public Page<MrpCellMaterial> getPageMrpCellMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpCellMaterial> spec = (Specification<MrpCellMaterial>) (root, query, criteriaBuilder) -> {
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
        return mrpCellMaterialRepository.findAll(spec, pageable);
    }

    @Override
    public Page<MrpAryMaterial> getPageMrpAryMaterial(int page, int limit, String ver, String searchProduct, String searchMaterialGroup, String searchMaterial, String searchSupplier) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "materialGroup", "material");
        Specification<MrpAryMaterial> spec = (Specification<MrpAryMaterial>) (root, query, criteriaBuilder) -> {
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
        return mrpAryMaterialRepository.findAll(spec, pageable);
    }
}
