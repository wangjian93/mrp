package com.ivo.mrp2.service.impl;


import com.ivo.common.utils.DateUtil;
import com.ivo.mrp2.entity.*;
import com.ivo.mrp2.key.MrpDataPrimaryKey;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.repository.MrpVerRepository;
import com.ivo.mrp2.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class MrpService2Impl implements MrpService2 {

    private DpsService dpsService;

    private MrpVerRepository mrpVerRepository;

    private BomService bomService;

    private DemandService demandService;

    private MrpDataRepository mrpDataRepository;

    private MaterialLossRateService materialLossRateService;

    private SupplierArrivalPlanService supplierArrivalPlanService;

    private InventoryService inventoryService;

    @Autowired
    public MrpService2Impl(DpsService dpsService, MrpVerRepository mrpVerRepository, BomService bomService,
                           DemandService demandService, MrpDataRepository mrpDataRepository,
                           MaterialLossRateService materialLossRateService,
                           SupplierArrivalPlanService supplierArrivalPlanService,
                           InventoryService inventoryService) {
        this.dpsService = dpsService;
        this.mrpVerRepository = mrpVerRepository;
        this.bomService = bomService;
        this.demandService = demandService;
        this.mrpDataRepository = mrpDataRepository;
        this.materialLossRateService = materialLossRateService;
        this.supplierArrivalPlanService = supplierArrivalPlanService;
        this.inventoryService = inventoryService;
    }

    @Override
    public MrpVer getMrpVer(String mrpVer) {
        return mrpVerRepository.getOne(mrpVer);
    }

    @Override
    public String generateMrpVer(String dpsVer) {
        if(!dpsService.isExistVer(dpsVer)) return null;
        Date[] dates = dpsService.getDpsDateRange(dpsVer);
        return generateMrpVer(dpsVer, dates[0], dates[1]);
    }

    @Override
    public String generateMrpVer(String dpsVer, Date startDate, Date endDate) {
        if (dpsVer == null || dpsVer.equals("") || startDate == null || endDate == null) return null;
        String ver = dpsVer + "-" + mrpVerRepository.countByDpsVer(dpsVer);
        MrpVer mrpVer = new MrpVer();
        String plant = dpsService.getPlantByDpsVer(dpsVer);
        mrpVer.setMrpVer(ver);
        mrpVer.setDpsVer(dpsVer);
        mrpVer.setStartDate(startDate);
        mrpVer.setEndDate(endDate);
        mrpVer.setStatus(MrpVer.STATUS_WAIT);
        mrpVer.setPlant(plant);
        mrpVerRepository.save(mrpVer);
        log.info("生成MRP版本" + ver);
        return ver;
    }


    @Override
    public void computeDemand(String mrpVer) {
        log.info("STA>>展开BOM计算MRP版本的材料需求，MRP版本"+mrpVer);
        MrpVer m = getMrpVer(mrpVer);
        if(m == null) return;
        String dpsVer = m.getDpsVer();
        List<String> products = getProducts(dpsVer);
        if(products == null) return;
        for(String product : products) {
            generateDemand(dpsVer, product);
        }
        log.info("END>>");
    }

    @Override
    public List<String> getProducts(String dpsVer) {
        return dpsService.getProduct(dpsVer);
    }

    @Override
    public void generateDemand(String dpsVer, String product) {
        List<Dps> dpsList = dpsService.getDps(dpsVer, product);
        if(dpsList == null || dpsList.size() == 0) return;
        String plant = dpsService.getPlantByDpsVer(dpsVer);
        List<Bom> bomList = getBomMaterial(product, plant);
        if(bomList == null || bomList.size() == 0) return;
        log.info("展开"+product+"，BOM料号共" + bomList.size());
        List<Demand> demandList = new ArrayList<>();
        for(Dps dps : dpsList) {
            for(Bom bom : bomList) {
                Demand demand = new Demand();
                demand.setDpsVer(dpsVer);
                demand.setFabDate(dps.getFabDate());
                demand.setProduct(dps.getProduct());
                demand.setMaterial(bom.getMaterial());
                demand.setMaterialName(bom.getMaterialName());
                demand.setDemandQty(dps.getQty() * 1000 * bom.getUsageQty());
                demandList.add(demand);
            }
        }
        demandService.batchSave(demandList);
    }

    @Override
    public List<Bom> getBomMaterial(String product, String plant) {
        return bomService.getBomByProductAndPlant(product, plant);
    }

    @Override
    public void generateMrpData(String mrpVer) {
        log.info("STA>>生成MRP版本数据，MRP版本"+mrpVer);
        MrpVer m = getMrpVer(mrpVer);
        if(m == null) return;
        String dpsVer = m.getDpsVer();
        List<String> productList = dpsService.getProduct(dpsVer);
        if(productList == null || productList.size() == 0) return;
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            generateMrpData(mrpVer, product);
        }
        log.info("END>>");
    }

    @Override
    public void generateMrpData(String mrpVer, String product) {
        MrpVer m = getMrpVer(mrpVer);
        if(m == null) return;
        String dpsVer = m.getDpsVer();
        List<Demand> demandList = demandService.getDemand(dpsVer, product);
        if(demandList == null || demandList.size() == 0) return;
        for(Demand demand : demandList) {
            String material = demand.getMaterial();
            Date fabDate = demand.getFabDate();
            MrpData mrpData = mrpDataRepository.findByMrpVerAndFabDateAndMaterial(mrpVer, fabDate, material);
            double demandQty = demand.getDemandQty();
            // 需求量
            if(mrpData == null) {
                mrpData = new MrpData();
                mrpData.setMrpVer(mrpVer);
                mrpData.setFabDate(fabDate);
                mrpData.setMaterial(material);
                mrpData.setMaterialName(demand.getMaterialName());
                mrpData.setPlant(m.getPlant());
                mrpData.setProducts(product);
                mrpData.setDemandQty(demandQty);
            } else {
                String products = mrpData.getProducts() + "/" + product;
                demandQty = mrpData.getDemandQty() + demandQty;
                mrpData.setDemandQty(demandQty);
                mrpData.setProducts(products);
            }

            // 损耗量
            double lossRate = materialLossRateService.getMaterialLossRate(material);
            double lossQty = lossRate * demandQty;
            mrpData.setLossQty(lossQty);

            //到货量
            double arrivalQty = supplierArrivalPlanService.getMaterialArrivalPlanQty(material, fabDate);
            mrpData.setArrivalQty(arrivalQty);
            mrpDataRepository.save(mrpData);
        }
    }

    @Override
    public void computeBalance(String mrpVer) {
        log.info("STA>>计算材料的结余量，MRP版本"+mrpVer);
        List<String> materialList = getMaterial(mrpVer);
        if(materialList == null || materialList.size() == 0) return;
        int l = materialList.size();
        for(String material : materialList) {
            log.info("材料" + material + "，剩余" + l--);
            computeBalance(mrpVer, material);
        }
        log.info("END>>");
    }

    @Override
    public void computeBalance(String mrpVer, String material) {
        List<Date> dateList = getMrpCalendarList(mrpVer);
        if(dateList == null || dateList.size() == 0) return;
        List<MrpData> mrpDataList = new ArrayList<>();
        for(int i=0; i<dateList.size(); i++) {
            Date fabDate = dateList.get(i);
            MrpData mrpData = mrpDataRepository.findByMrpVerAndFabDateAndMaterial(mrpVer, fabDate, material);
            if(mrpData == null) {
                mrpData = new MrpData();
                mrpData.setMrpVer(mrpVer);
                mrpData.setFabDate(fabDate);
                mrpData.setMaterial(material);
                // 需求量
                mrpData.setDemandQty(0);
                // 损耗量
                mrpData.setDemandQty(0);
                // 到货量
                double arrivalQty = supplierArrivalPlanService.getMaterialArrivalPlanQty(material, fabDate);
                mrpData.setArrivalQty(arrivalQty);
            }
            if(i==0) {
                // 第一天：期初库存 - 当日耗损量 – 当日需求量
                Date firstDate = dateList.get(0);
                double beginInventoryQty = inventoryService.getBeginInventory(material, firstDate);
                double demandQty = mrpData.getDemandQty();
                double lossQty = mrpData.getLossQty();
                double balanceQty = beginInventoryQty - demandQty - lossQty;
                mrpData.setBalanceQty(balanceQty);
            } else {
                // 其他天：前一天计算的可用库存 + 前一天的到货 - 当日耗损量 – 当日需求量
                MrpData yesterdayMrpData = mrpDataList.get(i-1);
                double yesterdayBalanceQty = yesterdayMrpData.getBalanceQty();
                double yesterdayArrivalQty = yesterdayMrpData.getArrivalQty();
                double demandQty = mrpData.getDemandQty();
                double lossQty = mrpData.getLossQty();
                double balanceQty = yesterdayBalanceQty + yesterdayArrivalQty - demandQty - lossQty;
                mrpData.setBalanceQty(balanceQty);
            }
            mrpDataList.add(mrpData);
        }
        mrpDataRepository.saveAll(mrpDataList);
    }

    @Override
    public List<String> getMaterial(String mrpVer) {
        return mrpDataRepository.getMaterial(mrpVer);
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
