//package com.ivo.mrp2.service.impl;
//
//import com.ivo.common.utils.DoubleUtil;
//import com.ivo.mrp2.entity.*;
//import com.ivo.mrp2.repository.MrpDataRepository;
//import com.ivo.mrp2.repository.MrpVerRepository;
//import com.ivo.mrp2.service.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author wj
// * @version 1.0
// */
//@Slf4j
//@Service
//public class MrpService3Impl implements MrpService3 {
//
//    private DpsService dpsService;
//
//    private MrpVerRepository mrpVerRepository;
//
//    private MrpDataRepository mrpDataRepository;
//
//    @Autowired
//    public MrpService3Impl(DpsService dpsService, MrpVerRepository mrpVerRepository,
//                           MrpDataRepository mrpDataRepository
//                          ) {
//        this.dpsService = dpsService;
//        this.mrpVerRepository = mrpVerRepository;
//        this.mrpDataRepository = mrpDataRepository;
//    }
//
//    public MrpVer getMrpVer(String mrpVer) {
//        return mrpVerRepository.getOne(mrpVer);
//    }
//
//    @Override
//    public void generateMrpData(String mrpVer) {
//        log.info("STA>>生成MRP版本数据，MRP版本"+mrpVer);
//        MrpVer m = getMrpVer(mrpVer);
//        if(m == null) return;
//        String dpsVer = m.getDpsVer();
//        List<Map> mapList = dpsService.summaryMaterial(dpsVer);
//        if(mapList == null || mapList.size()==0) return;
//        List<MrpData> mrpDataList = new ArrayList<>();
//        for(Map map : mapList) {
//            MrpData mrpData = new MrpData();
//            mrpData.setMrpVer(mrpVer);
//            mrpData.setMaterial( (String) map.get("material"));
//            mrpData.setFabDate( (Date) map.get("fabDate"));
//            double demandQty = 0;
//            double lossRate = 0;
//            double arrivalQty = 0;
//            if(map.get("demandQty") != null) {
//                demandQty = (double)  map.get("demandQty");
//            }
//            if(map.get("lossRate") != null) {
//                lossRate = (double)  map.get("lossRate");
//            }
//            if(map.get("arrivalQty") != null) {
//                arrivalQty = (double)  map.get("arrivalQty");
//            }
//            BigDecimal bigDecimal = new BigDecimal(demandQty);
//            demandQty = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//            double lossQty = DoubleUtil.multiply(lossRate, demandQty);
//            mrpData.setDemandQty(demandQty);
//            mrpData.setLossQty(lossQty);
//            mrpData.setArrivalQty(arrivalQty);
//            mrpDataList.add(mrpData);
//        }
//
//        mrpDataRepository.saveAll(mrpDataList);
//        log.info("END>>");
//    }
//}
