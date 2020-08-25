package com.ivo.mrp2.service.impl;

import com.ivo.mrp2.entity.Demand;
import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.key.MrpDataPrimaryKey;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.service.DemandService;
import com.ivo.mrp2.service.DpsService;
import com.ivo.mrp2.service.MrpDataService;
import com.ivo.mrp2.service.MrpVerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author wj
 * @version 1.0
 */
 @Service
public class MrpDataServiceImpl implements MrpDataService {

     private MrpDataRepository mrpDataRepository;

     private DemandService demandService;

     private MrpVerService mrpVerService;

     private DpsService dpsService;

     @Autowired
    public MrpDataServiceImpl(MrpDataRepository mrpDataRepository, DemandService demandService, MrpVerService mrpVerService,
                              DpsService dpsService) {
        this.mrpDataRepository = mrpDataRepository;
        this.demandService = demandService;
        this.mrpVerService = mrpVerService;
        this.dpsService = dpsService;
    }

    @Override
    public List<MrpData> getMrpData(String mrpVer) {
        return mrpDataRepository.findByMrpVer(mrpVer);
    }

    @Override
    public List<MrpData> getShortMrpData(String mrpVer, List<String> materialList) {
        return mrpDataRepository.findByMrpVerAndShortQtyLessThanAndMaterialIn(mrpVer,0, materialList);
    }

    @Override
    public List<MrpData> getMrpData(String mrpVer, List<String> materialList) {
        return mrpDataRepository.findByMrpVerAndMaterialIn(mrpVer, materialList);
    }

    @Override
    public List<MrpData> getMrpData(String mrpVer, List<String> materialList, Date startDate, Date endDate) {
        return mrpDataRepository.findByMrpVerAndMaterialInAndFabDateGreaterThanEqualAndFabDateLessThanEqual(mrpVer, materialList, startDate, endDate);
    }

    @Override
    public List<MrpData> getMrpData(String mrpVer, String material) {
        return mrpDataRepository.findByMrpVerAndMaterial(mrpVer, material);
    }

    @Override
    public List<Map> getMaterialDemandDetail(String mrpVer, String material, Date fabDate) {
         List<Demand> demandList = new ArrayList<>();
         String[] dpsVers = mrpVerService.getMrpVer(mrpVer).splitDpsVer();
         for(String dpsVer : dpsVers) {
             demandList.addAll( demandService.getDemand(dpsVer, fabDate, material));
             demandList.addAll( demandService.getDemand(dpsService.getDpsVer(dpsVer).getSmallVer(), fabDate, material));
         }
         List<Map> mapList = new ArrayList<>();
         for(Demand demand : demandList) {
             Map<String, Object> map = new HashMap<>();
             map.put("dpsVer", demand.getDpsVer());
             map.put("product", demand.getProduct());
             map.put("dpsQty", demand.getDpsQty());
             map.put("usageQty", demand.getUsageQty());
             map.put("slice", demand.getSlice());
             map.put("substituteRate", demand.getSubstituteRate());
             map.put("demandQty", demand.getDemandQty());
             mapList.add(map);
         }
        return mapList;
    }

    @Override
    public void editBalance(String mrpVer, String material, Date fabDate, double balanceQty, String memo) {
         MrpData mrpData = getMrpData(mrpVer, material, fabDate);
         mrpData.setBalanceQtyHis(mrpData.getBalanceQty());
         mrpData.setBalanceQty(balanceQty);
         mrpData.setModifyBalanceFlag(true);
         mrpData.setMemo(memo);
         mrpDataRepository.save(mrpData);
    }

    @Override
    public MrpData getMrpData(String mrpVer, String material, Date fabDate) {
        MrpDataPrimaryKey mrpDataPrimaryKey = new MrpDataPrimaryKey(mrpVer, fabDate, material);
        return  mrpDataRepository.findById(mrpDataPrimaryKey).orElse(null);
    }

    @Override
    public void save(MrpData mrpData) {
        mrpDataRepository.save(mrpData);
    }

    @Override
    public void save(List<MrpData> mrpDataList) {
        mrpDataRepository.saveAll(mrpDataList);

    }

    @Override
    public Page<String> getPageMaterialForShort(int page, int limit, List<String> verList, String material) {
        return null;
    }
}
