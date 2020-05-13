//package com.ivo.mrp.service.impl;
//
//import com.ivo.common.utils.DateUtil;
//import com.ivo.mrp.entity.Dps;
//import com.ivo.mrp.repository.DpsRepository;
//import com.ivo.mrp.service.DpsService;
//import com.ivo.rest.dps.entity.RestDps;
//import com.ivo.rest.dps.service.RestDpsService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author wj
// * @version 1.0
// */
//@Slf4j
////@Service
//public class DpsServiceImpl implements DpsService {
//
//    private DpsRepository dpsRepository;
//
//    private RestDpsService restDpsService;
//
//    @Autowired
//    public DpsServiceImpl(DpsRepository dpsRepository, RestDpsService restDpsService) {
//        this.dpsRepository = dpsRepository;
//        this.restDpsService = restDpsService;
//    }
//
//    @Override
//    public List<Dps> getDps(String ver) {
//        return dpsRepository.findByVer(ver);
//    }
//
//    @Override
//    public Dps getDps(String ver, String fab, String product, Date fabDate) {
//        return dpsRepository.getDpsByVerAndFabAndProductAndFabDate(ver, fab, product, fabDate);
//    }
//
//    @Override
//    public String importDpsByVer(InputStream excel, String fileName) {
//        return null;
//    }
//
//    @Override
//    public void syncDpsByVer(String ver) {
//        log.info("START 同步DPS >>" + ver);
//        if(getDps(ver).size() > 0) return;
//        List<RestDps> restDpsList = restDpsService.getDpsByVer(ver);
//        if(restDpsList== null || restDpsList.size()==0) return;
//        List<Dps> dpsList = new ArrayList<>();
//        for(RestDps restDps : restDpsList) {
//            Dps dps = new Dps();
//            dps.setProduct(restDps.getProd_id());
//            dps.setModel(restDps.getModel_id());
//            dps.setFab(restDps.getFab_id());
//            dps.setFabDate(restDps.getFab_date());
//            dps.setQty(restDps.getBpc_qty());
//            dps.setVer(restDps.getDps_ver());
//            dpsList.add(dps);
//        }
//        dpsRepository.saveAll(dpsList);
//        log.info("END 同步DPS >>" + ver);
//    }
//
//    @Override
//    public List<String> getDpsVer() {
//        return restDpsService.getDpsVer();
//    }
//
//    @Override
//    public Date[] getStartAndEndDate(String ver) {
//        // DPS版本'20190418'
//        int year = Integer.parseInt(ver.substring(0, 4));
//        int mont = Integer.parseInt(ver.substring(4, 6));
//        Date firstDay = DateUtil.getFirstDayOfMonth1(year, mont);
//        Date lastDay = DateUtil.getLastDayOfMonth1(year, mont);
//        return new Date[] {firstDay, lastDay};
//    }
//}
