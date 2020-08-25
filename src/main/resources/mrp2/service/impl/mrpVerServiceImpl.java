package com.ivo.mrp2.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.mrp2.entity.MrpVer;
import com.ivo.mrp2.repository.MrpVerRepository;
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

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class mrpVerServiceImpl implements MrpVerService {

    private MrpVerRepository mrpVerRepository;

    @Autowired
    public mrpVerServiceImpl(MrpVerRepository mrpVerRepository) {
        this.mrpVerRepository = mrpVerRepository;
    }

    @Override
    public Page<MrpVer> getPageMrpVer(int page, int limit, String dpsVer, String mpsVer, String plant, Date fromDate, Date toDate) {
        //排序
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Pageable pageable = PageRequest.of(page, limit, sort);
        //封装查询对象Specification  这是个自带的动态条件查询
        Specification<MrpVer> spec = (Specification<MrpVer>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(StringUtils.isNotEmpty(dpsVer)) {
                predicates.add(criteriaBuilder.like(root.get("dpsVer"), "%"+dpsVer+"%"));
            }
            if(StringUtils.isNotEmpty(mpsVer)) {
                predicates.add(criteriaBuilder.like(root.get("mpsVer"), "%"+mpsVer+"%"));
            }
            if(StringUtils.isNotEmpty(plant)) {
                predicates.add(criteriaBuilder.like(root.get("plant"), "%"+plant+"%"));
            }
            if(fromDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), new java.sql.Date(fromDate.getTime())));
            }
            if(toDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), new java.sql.Date(toDate.getTime())));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return mrpVerRepository.findAll(spec, pageable);
    }

    @Override
    public MrpVer getMrpVer(String ver) {
        return mrpVerRepository.findById(ver).orElse(null);
    }

    @Override
    public void saveMrpVer(MrpVer mrpVer) {
        mrpVerRepository.save(mrpVer);
    }

    @Override
    public List<Date> getMrpCalendarList(String ver) {
        MrpVer mrpVer = getMrpVer(ver);
        return DateUtil.getCalendar(mrpVer.getStartDate(), mrpVer.getEndDate());
    }
}
