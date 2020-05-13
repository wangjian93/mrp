package com.ivo.mrp2.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivo.common.utils.DateUtil;
import com.ivo.mrp2.entity.*;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.repository.MrpVerRepository;
import com.ivo.mrp2.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MrpServiceImpl implements MrpService {

    private MrpVerRepository mrpVerRepository;

    private MrpDataRepository mrpDataRepository;

    private DpsService dpsService;

    private BomService bomService;

    private DemandService demandService;

    private MaterialLossRateService lossRateService;

    private InventoryService inventoryService;

    private MaterialDailyBalanceService balanceService;

    private SupplierArrivalPlanService arrivalPlanService;


    @Autowired
    public MrpServiceImpl(MrpVerRepository mrpVerRepository, DpsService dpsService, BomService bomService,
                          DemandService demandService,
                          MaterialLossRateService lossRateService,
                          InventoryService inventoryService,
                          MaterialDailyBalanceService balanceService,
                          SupplierArrivalPlanService arrivalPlanService,
                          MrpDataRepository mrpDataRepository) {
        this.mrpVerRepository = mrpVerRepository;
        this.dpsService = dpsService;
        this.bomService = bomService;
        this.demandService = demandService;
        this.lossRateService = lossRateService;
        this.inventoryService = inventoryService;
        this.balanceService = balanceService;
        this.arrivalPlanService = arrivalPlanService;
        this.mrpDataRepository = mrpDataRepository;
    }


//
//    @Override
//    public void dpsExpandBom(String mrpVer) {
//        log.info("START>> DPS展开BOM，MRP版本" + mrpVer);
//        MrpVer mrp = getMrpVer(mrpVer);
//        if(mrp == null) return;
//        List<Dps> dpsList = dpsService.getDps(mrp.getDpsVer());
//        if(dpsList == null) return;
//        for(Dps dps : dpsList) {
//            log.info("展开" + dps.getProduct() + "/" + dps.getFabDate());
//            List<Bom> bomList = bomService.getBomByProductAndPlant(dps.getFab(), dps.getProduct());
//            if(bomList == null) continue;
//            List<MaterialDailyDemandTemp> demandList = new ArrayList<>();
//            for(Bom bom : bomList) {
//                MaterialDailyDemandTemp demand = new MaterialDailyDemandTemp();
//                demand.setMrpVer(mrpVer);
//                demand.setFabDate(dps.getFabDate());
//                demand.setMaterial(bom.getMaterial());
//                demand.setMaterialName(bom.getMaterialName());
//                demand.setDemandQty(bom.getUsageQty()*1000*dps.getQty());
//                demand.setFab(dps.getFab());
//                demand.setProduct(dps.getProduct());
//                demandList.add(demand);
//            }
//            demandService.batchSaveTemp(demandList);
//        }
//        log.info("END>>");
//    }
//
//    @Override
//    public void computeLoss(String mrpVer) {
////        log.info("START>> 计算材料的每日需求量的损耗数量, MRP版本" + mrpVer);
////        List<Map> mapList = demandService.summaryMaterialDemand(mrpVer);
////        if(mapList == null) return;
////        ObjectMapper objectMapper = new ObjectMapper();
////        List<Demand> list = new ArrayList<>();
////        for(Map map : mapList) {
////            Demand demand = objectMapper.convertValue(map, Demand.class);
////            Double lossRate = lossRateService.getMaterialLossRate(demand.getMaterial());
////            demand.setLossQty(demand.getDemandQty() * lossRate);
////            list.add(demand);
////        }
////        demandService.batchSave(list);
////        log.info("END>>");
//    }
//
//    @Override
//    public void computeBalance(String mrpVer) {
////        log.info("START>> 计算材料的每日结余数量, MRP版本" + mrpVer);
////        List<Date> dateList = getMrpCalendarList(mrpVer);
////        if(dateList == null || dateList.size() == 0) return;
////        List<String> materialList = demandService.summaryMaterial(mrpVer);
////        if(materialList == null || materialList.size() ==0) return;
////        log.info("共有料：" + materialList.size() + "，计算天数：" + dateList.size());
////        long l = materialList.size();
////        for(String material : materialList) {
////            log.info("计算料号" + material + ",还剩" + --l);
////            List<MaterialDailyBalance> balanceList = new ArrayList<>();
////
////            // 第一天：初期库存 - 当日耗损量 – 当日需求量
////            Date firstDate = dateList.get(0);
////            Double first_beginInventoryQty = inventoryService.getBeginInventory(material, firstDate);
////            Double first_demandQty;
////            Double first_lossQty;
////            Demand first_demand = demandService.getMaterialDailyDemandLoss(mrpVer, material, firstDate);
////            if(first_demand != null) {
////                first_demandQty = first_demand.getDemandQty();
////                first_lossQty = first_demand.getLossQty();
////            } else {
////                first_demandQty = 0D;
////                first_lossQty = 0D;
////            }
////            MaterialDailyBalance first_balance = new MaterialDailyBalance(mrpVer, material, "");
////            first_balance.setFabDate(firstDate);
////            first_balance.setBalanceQty(first_beginInventoryQty - first_demandQty - first_lossQty);
////            balanceList.add(first_balance);
////
////            // 其他天：前一天计算的可用库存 + 前一天的到货 - 当日耗损量 – 当日需求量
////            for(int i=1; i<dateList.size(); i++) {
////                Date currentDate = dateList.get(i);
////                Double yesterdayBalance = balanceList.get(i-1).getBalanceQty();
////                Double demandQty;
////                Double lossQty;
////                Double yesterdayArrivalPlanQty = arrivalPlanService.getMaterialArrivalPlanQty(material, dateList.get(i-1));
////                Demand demand;
////                demand = demandService.getMaterialDailyDemandLoss(mrpVer, material, currentDate);
////                if(demand != null) {
////                    demandQty = demand.getDemandQty();
////                    lossQty = demand.getLossQty();
////                } else {
////                    demandQty = 0D;
////                    lossQty = 0D;
////                }
////                MaterialDailyBalance balance = new MaterialDailyBalance(mrpVer, material, "");
////                balance.setFabDate(currentDate);
////                balance.setBalanceQty(yesterdayBalance + yesterdayArrivalPlanQty - demandQty - lossQty);
////                balanceList.add(balance);
////            }
////            balanceService.batchSave(balanceList);
////        }
////        log.info("END>>");
//    }
//
//    @Override
//    public List<Date> getMrpCalendarList(String mrpVer) {
//        MrpVer mrp = getMrpVer(mrpVer);
//        List<java.util.Date> list = DateUtil.getCalendar(mrp.getStartDate(), mrp.getEndDate());
//        List<Date> sqlDateList = new ArrayList<>();
//        if(list == null) return sqlDateList;
//        for(java.util.Date date : list) {
//            sqlDateList.add(new Date(date.getTime()));
//        }
//        return sqlDateList;
//    }
//
//    @Override
//    public List<String> getMrpVer() {
//        return mrpVerRepository.getMrpVer();
//    }


    @Override
    public MrpVer getMrpVer(String mrpVer) {
        return mrpVerRepository.getOne(mrpVer);
    }

    @Override
    public List<String> getMrpVerStr() {
        return mrpVerRepository.getMrpVer();
    }

    @Override
    public List<MrpVer> getMrpVer() {
        return mrpVerRepository.findAll();
    }

    @Override
    public void generateMrp(String dpsVer) {
        log.info("START>> 选择DPS版本" + dpsVer + "生成MRP版本");
        if(!dpsService.isExistVer(dpsVer)) return;
        String ver = dpsVer + "_" + mrpVerRepository.countByDpsVer(dpsVer);
        MrpVer mepVer = new MrpVer();
        mepVer.setMrpVer(ver);
        mepVer.setDpsVer(dpsVer);
        mepVer.setPlant(dpsService.getPlantByDpsVer(dpsVer));
        mepVer.setStatus(MrpVer.STATUS_WAIT);
        mrpVerRepository.save(mepVer);
        log.info("END>> " + ver);
    }

    @Override
    public void determineDateRange(String mrpVer) {
        log.info("START>> 确定MRP的日期区间，MRP版本" + mrpVer);
        // 将DPS的日期区间作为MRP运算的日期区间
        MrpVer mrp = getMrpVer(mrpVer);
        if(mrp == null) return;
        Date[] dates = dpsService.getDpsDateRange(mrp.getDpsVer());
        if(dates == null || dates.length<2 || dates[0]==null || dates[1]==null) {
            log.warn("DPS日期区间确定失败");
            return;
        }
        mrp.setStartDate(dates[0]);
        mrp.setEndDate(dates[1]);
        mrpVerRepository.save(mrp);
        log.info("END>>");
    }

    @Override
    public void expandDps(String mrpVer) {
        log.info("START>> 展开Bom，MRP版本" + mrpVer);
        MrpVer mrp = getMrpVer(mrpVer);
        if(mrp == null) return;
        List<String> productList = dpsService.getProduct(mrp.getDpsVer());
        if(productList == null) return;
        int l = productList.size();
        for(String product : productList) {
            log.info("展机种" + product + "，剩余" + l--);
            expandProduct(mrp.getDpsVer(), product);
        }
        log.info("END>>");
    }

    @Override
    public void expandProduct(String mrpVer, String product) {
        MrpVer mrp = getMrpVer(mrpVer);
        if(mrp == null) return;
        List<Bom> bomList = bomService.getBomByProductAndPlant(product, mrp.getPlant());
        if(bomList == null) return;
        List<Dps> dpsList = dpsService.getDps(mrp.getDpsVer(), product);
        if(dpsList == null) return;
        List<Demand> demandList = new ArrayList<>();
        for(Dps dps : dpsList) {
            for(Bom bom : bomList) {
                Demand demand = new Demand();
                demand.setDpsVer(dps.getDpsVer());
                demand.setFabDate(dps.getFabDate());
                demand.setProduct(dps.getProduct());
                demand.setMaterial(bom.getMaterial());
                demand.setMaterialName(bom.getMaterialName());
                demand.setDemandQty(dps.getQty()*1000*bom.getUsageQty());
                demandList.add(demand);
            }
        }
        demandService.batchSave(demandList);
    }


    @Override
    public void computeDemand(String mrpVer) {
//        log.info("START>> 计算材料的需求量, MRP版本" + mrpVer);
//        MrpVer mrp = getMrpVer(mrpVer);
//        if(mrp == null) return;
//        List<String> materialList  = demandService.summaryMaterial(mrp.getDpsVer());
//        if(materialList == null) return;
//        List<Date> dateList = getMrpCalendarList(mrpVer);
//        int l = materialList.size();
//        for(String material : materialList) {
//            log.info("计算材料"+material+"的需求量, 剩余" + l--);
//            List<MrpData> mrpDataList = new ArrayList<>();
//            for (Date fabDate : dateList) {
//                List<Demand> demandList = demandService.getDemand(mrp.getDpsVer(), fabDate, material);
//                double demandQty = 0;
//                String product = "";
//                if(demandList!=null) {
//                    for(Demand demand : demandList) {
//                        demandQty += demand.getDemandQty();
//                        if(StringUtils.isEmpty(product)) {
//                            product += demand.getProduct();
//                        } else {
//                            product += "/" + demand.getProduct();
//                        }
//                    }
//                }
//                MrpData mrpData = new MrpData();
//                mrpData.setMrpVer(mrpVer);
//                mrpData.setMaterial(material);
//                mrpData.setFabDate(fabDate);
//                mrpData.setProducts(product);
//                mrpData.setPlant(mrp.getPlant());
//                mrpData.setDemandQty(demandQty);
//                mrpDataList.add(mrpData);
//            }
//            mrpDataRepository.saveAll(mrpDataList);
//        }
//        log.info("END>>");
    }

    @Override
    public void computeLoss(String mrpVer) {
        log.info("START>> 计算材料的需求量, MRP版本" + mrpVer);
        List<MrpData> mrpDataList = mrpDataRepository.findByMrpVer(mrpVer);
        int l = mrpDataList.size();
        for(MrpData mrpData : mrpDataList) {
            log.info("剩余" + l--);
            double lossRate = lossRateService.getMaterialLossRate(mrpData.getMaterial());
            mrpData.setLossQty(mrpData.getDemandQty()*lossRate);
        }
        mrpDataRepository.saveAll(mrpDataList);
        log.info("END>>");
    }

    @Override
    public void computeArrival(String mrpVer) {
        log.info("START>> 计算材料的到货量, MRP版本" + mrpVer);
        List<MrpData> mrpDataList = mrpDataRepository.findByMrpVer(mrpVer);
        int l = mrpDataList.size();
        for(MrpData mrpData : mrpDataList) {
            log.info("剩余" + l--);
            double arrivalQty = arrivalPlanService.getMaterialArrivalPlanQty(mrpData.getMaterial(), mrpData.getFabDate());
            mrpData.setLossQty(arrivalQty);
        }
        mrpDataRepository.saveAll(mrpDataList);
        log.info("END>>");
    }

    @Override
    public void computeBalance(String mrpVer) {

    }

    @Override
    public List<Date> getMrpCalendarList(String mrpVer) {
        MrpVer mrp = getMrpVer(mrpVer);
        List<java.util.Date> list = DateUtil.getCalendar(mrp.getStartDate(), mrp.getEndDate());
        List<Date> sqlDateList = new ArrayList<>();
        if(list == null) return sqlDateList;
        for(java.util.Date date : list) {
            sqlDateList.add(new Date(date.getTime()));
        }
        return sqlDateList;
    }


}
