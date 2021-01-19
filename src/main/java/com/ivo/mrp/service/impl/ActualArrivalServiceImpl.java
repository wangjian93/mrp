package com.ivo.mrp.service.impl;

import com.ivo.common.utils.StringUtil;
import com.ivo.mrp.entity.ActualArrival;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.repository.ActualArrivalRepository;
import com.ivo.mrp.service.ActualArrivalService;
import com.ivo.mrp.service.MaterialService;
import com.ivo.mrp.service.SupplierService;
import com.ivo.rest.RestService;
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
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class ActualArrivalServiceImpl implements ActualArrivalService {

    private RestService restService;

    private ActualArrivalRepository actualArrivalRepository;

    private SupplierService supplierService;

    private MaterialService materialService;

    @Autowired
    public ActualArrivalServiceImpl(RestService restService, ActualArrivalRepository actualArrivalRepository,
                                    SupplierService supplierService, MaterialService materialService) {
        this.restService = restService;
        this.actualArrivalRepository = actualArrivalRepository;
        this.supplierService = supplierService;
        this.materialService = materialService;
    }

    @Override
    public void syncActualArrival() {
        Calendar c = Calendar.getInstance();
        c.setTime(new java.util.Date());
        c.add(Calendar.DATE, -1);
        java.util.Date yesterday = c.getTime(); //前一天
        syncActualArrival(new Date(yesterday.getTime()));
    }

    @Override
    public void syncActualArrival(Date fabDate) {
        if(getActualArrival(fabDate).size() > 0) return;
        log.info("同步实际到货数据>> START");
        List<Map>list = restService.getActualArrivalQty(fabDate);
        if(list==null || list.size()==0) {
            log.info("同步实际到货数据>> END 没有数据");
            return;
        }
        List<ActualArrival> actualArrivalList = new ArrayList<>();
        for(Map map : list) {
            String material = (String)map.get("material");
            String supplierCode = (String) map.get("supplierCode");
            String orderNumber = (String) map.get("orderNumber");
            double qty = ((BigDecimal) map.get("qty")).doubleValue();
            String werks = (String) map.get("werks");
            String lgort = (String) map.get("lgort");

            ActualArrival actualArrival = new ActualArrival();
            actualArrival.setFabDate(fabDate);
            actualArrival.setMaterial(material);
            actualArrival.setSupplierCode(supplierCode);
            actualArrival.setQty(qty);
            actualArrival.setOrderNumber(orderNumber);
            actualArrival.setWerks(werks);
            actualArrival.setLgort(lgort);

            Supplier supplier = supplierService.getSupplier(supplierCode);
            if(supplier != null) {
                actualArrival.setSupplierName(supplier.getSupplierName());
            }
            actualArrival.setMaterialName(materialService.getMaterialName(material));
            actualArrival.setMaterialGroup(materialService.getMaterialGroup(material));
            actualArrivalList.add(actualArrival);
        }
        actualArrivalRepository.saveAll(actualArrivalList);
        log.info("同步实际到货数据>> END");
    }

    @Override
    public List<ActualArrival> getActualArrival(Date fabDate) {
        return actualArrivalRepository.findByFabDate(fabDate);
    }

    @Override
    public List<ActualArrival> getActualArrival(Date fabDate, String material) {
        return actualArrivalRepository.findByFabDateAndMaterial(fabDate, material);
    }

    @Override
    public double getActualArrivalQty(Date fabDate, String material) {
        Double d = actualArrivalRepository.getActualArrivalQty(fabDate, material);
        return d==null ? 0 : d;
    }

    @Override
    public double getActualArrivalQty(Date fabDate, String material, String fab) {
        if(StringUtils.equalsIgnoreCase(fab, "LCM1")) {
            fab = "3000";
        } else {
            fab = "1000";
        }
        Double d = actualArrivalRepository.getActualArrivalQty(fabDate, material, fab);
        return d==null ? 0 : d;
    }

    @Override
    public List<ActualArrival> getActualArrival(Date fabDate, String material, String fab) {
        if(StringUtils.equalsIgnoreCase(fab, "LCM1")) {
            fab = "3000";
        } else {
            fab = "1000";
        }
        return actualArrivalRepository.findByFabDateAndMaterialAndWerks(fabDate, material, fab);
    }

    @Override
    public Page<ActualArrival> queryActualArrival(int page, int limit, Date fabDate, String materialGroup, String material, String supplierCode, String fab) {
        Pageable pageable = PageRequest.of(page, limit, Sort.Direction.DESC, "fabDate", "material", "supplierCode");
        Specification<ActualArrival> spec = (Specification<ActualArrival>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(fabDate != null) {
                predicates.add(criteriaBuilder.equal(root.get("fabDate"), fabDate));
            }
            if(StringUtils.isNotEmpty(material)) {
                predicates.add(criteriaBuilder.like(root.get("material"), material+"%"));
            }
            if(StringUtils.isNotEmpty(materialGroup)) {
                predicates.add(criteriaBuilder.like(root.get("materialGroup"), materialGroup+"%"));
            }
            if(StringUtils.isNotEmpty(supplierCode)) {
                predicates.add(criteriaBuilder.like(root.get("supplierCode"), supplierCode+"%"));
            }
            if(StringUtils.isNotEmpty(fab)) {
                predicates.add(criteriaBuilder.like(root.get("werks"), fab+"%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return actualArrivalRepository.findAll(spec, pageable);
    }

    @Override
    public List<ActualArrival> getActualArrival(String fab, Date startDate, Date endDate, String material, String supplierCode) {
        if(StringUtils.equalsIgnoreCase(fab, "LCM1")) {
            fab = "3000";
        } else {
            fab = "1000";
        }
        return actualArrivalRepository.findByWerksAndMaterialAndSupplierCodeAndFabDateGreaterThanEqualAndFabDateLessThanEqual(
                fab, material, supplierCode, startDate, endDate
        );
    }
}
