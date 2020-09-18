package com.ivo.mrp.service.impl;

import com.ivo.mrp.entity.MonthSettle;
import com.ivo.mrp.repository.MonthSettleRepository;
import com.ivo.mrp.service.MaterialGroupService;
import com.ivo.mrp.service.MonthSettleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MonthSettleServiceImpl implements MonthSettleService {

    private MonthSettleRepository monthSettleRepository;

    private MaterialGroupService materialGroupService;

    @Autowired
    public MonthSettleServiceImpl(MonthSettleRepository monthSettleRepository, MaterialGroupService materialGroupService) {
        this.monthSettleRepository = monthSettleRepository;
        this.materialGroupService = materialGroupService;
    }

    private  MonthSettle getMonthSettle(Long id) {
        return monthSettleRepository.findById(id).orElse(null);
    }

    @Override
    public void addMonthSettle(String fab, String product, String materialGroup, String month,
                               java.sql.Date settleDate, double settleQty, String user) {
        MonthSettle monthSettle = monthSettleRepository.findFirstByFabAndProductAndMaterialGroupAndMonthAndValidFlag(fab, product, materialGroup, month, true);
        if(monthSettle != null) throw new RuntimeException("该机种的物料组已结算过");
        monthSettle = new MonthSettle();
        monthSettle.setFab(fab);
        monthSettle.setProduct(product);
        monthSettle.setMaterialGroup(materialGroup);
        monthSettle.setMonth(month);
        monthSettle.setSettleDate(settleDate);
        monthSettle.setSettleQty(settleQty);
        monthSettle.setMaterialGroupName(materialGroupService.getMaterialGroupName(materialGroup));
        monthSettle.setCreator(user);
        monthSettle.setUpdater(user);
        monthSettleRepository.save(monthSettle);
    }

    @Override
    public void delMonthSettle(Long id, String user) {
        MonthSettle monthSettle = getMonthSettle(id);
        if(monthSettle == null) return;
        monthSettle.setValidFlag(false);
        monthSettle.setUpdater(user);
        monthSettle.setUpdateDate(new Date());
        monthSettleRepository.save(monthSettle);
    }

    @Override
    public List<MonthSettle> getMonthSettle(String fab, String month) {
        return monthSettleRepository.findByFabAndMonthAndValidFlag(fab, month, true);
    }
}
