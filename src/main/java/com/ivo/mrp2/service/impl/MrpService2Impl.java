package com.ivo.mrp2.service.impl;


import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp2.entity.*;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.repository.MrpVerRepository;
import com.ivo.mrp2.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ProductSliceService productSliceService;

    private MrpMaterialService mrpMaterialService;

    private MaterialSupplierService materialSupplierService;

    @Autowired
    public MrpService2Impl(DpsService dpsService, MrpVerRepository mrpVerRepository, BomService bomService,
                           DemandService demandService, MrpDataRepository mrpDataRepository,
                           MaterialLossRateService materialLossRateService,
                           SupplierArrivalPlanService supplierArrivalPlanService,
                           InventoryService inventoryService,
                           ProductSliceService productSliceService,
                           MrpMaterialService mrpMaterialService, MaterialSupplierService materialSupplierService) {
        this.dpsService = dpsService;
        this.mrpVerRepository = mrpVerRepository;
        this.bomService = bomService;
        this.demandService = demandService;
        this.mrpDataRepository = mrpDataRepository;
        this.materialLossRateService = materialLossRateService;
        this.supplierArrivalPlanService = supplierArrivalPlanService;
        this.inventoryService = inventoryService;
        this.productSliceService = productSliceService;
        this.mrpMaterialService =  mrpMaterialService;
        this.materialSupplierService = materialSupplierService;
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
    public MrpVer getMrpVer(String mrpVer) {
        return mrpVerRepository.findById(mrpVer).orElse(null);
    }

    @Override
    public Page<MrpVer> getMrpVer(int page, int limit) {
        Sort sort = new Sort(Sort.Direction.DESC,"mrpVer");
        Pageable pageable = PageRequest.of(page, limit, sort);
        return mrpVerRepository.findAll(pageable);
    }




    @Override
    public List<MrpData> getMrpData(String mrpVer, String material) {
        return mrpDataRepository.findByMrpVerAndMaterial(mrpVer, material);
    }

    @Override
    public List<MrpData> getMrpData(String mrpVer, List<String> materials) {
        return mrpDataRepository.findByMrpVerAndMaterialIn(mrpVer, materials);
    }

    @Override
    public MrpData getMrpData(String mrpVer, String material, Date fabDate) {
        return mrpDataRepository.findByMrpVerAndFabDateAndMaterial(mrpVer, fabDate, material);
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
        List<String> products = dpsService.getProduct(dpsVer);
        if(products == null) return;
        for(String product : products) {
            computeDemand(dpsVer, product);
        }
        log.info("END>>");
    }

    @Override
    public void computeDemand(String dpsVer, String product) {
        List<Dps> dpsList = dpsService.getDps(dpsVer, product);
        if(dpsList == null || dpsList.size() == 0) return;
        String plant = dpsService.getPlantByDpsVer(dpsVer);
        List<Bom> bomList = bomService.getBomByProductAndPlant(product, plant);
        if(bomList == null || bomList.size() == 0) return;
        List<Demand> demandList = new ArrayList<>();
        double slice = productSliceService.getProductSliceService(product);
        for(Dps dps : dpsList) {
            for(Bom bom : bomList) {
                Demand demand = new Demand();
                demand.setDpsVer(dpsVer);
                demand.setFabDate(dps.getFabDate());
                demand.setProduct(dps.getProduct());
                demand.setMaterial(bom.getMaterial());
                demand.setMaterialName(bom.getMaterialName());
                double dpsQty = dps.getQty();
                double usageQty = bom.getUsageQty();
                // LCM ： DPS需求量 * Bon使用量
                // CELL ： DPS需求量 * 1000 * 切片数 * Bon使用量
                //  需求量不保留小数，向上取整
                double demandQty = 0;
                if(StringUtils.equalsIgnoreCase(dps.getFab(), "CELL")) {
                    demand.setSlice(slice);
                    demandQty = DoubleUtil.multiply(dpsQty , usageQty, slice, 1000d);
                } else if(StringUtils.equalsAnyIgnoreCase(dps.getFab(), "LCM1", "LCM2")) {
                    demandQty = DoubleUtil.multiply(dpsQty , usageQty);
                }
                demand.setFab(dps.getFab());
                demand.setDpsQty(dpsQty);
                demand.setUsageQty(usageQty);
                demand.setDemandQty(demandQty);
                demandList.add(demand);
            }
        }
        demandService.batchSave(demandList);
    }




    @Override
    public void generateMrpData(String mrpVer) {
        log.info("STA>>生成MRP版本数据，MRP版本"+mrpVer);
        MrpVer m = getMrpVer(mrpVer);
        if(m == null) return;
        String dpsVer = m.getDpsVer();
        List<String> materialList = demandService.getMaterial(dpsVer);
        if(materialList==null || materialList.size()==0) return;
        // 利用缓存减少轮询数据库
        MrpVer mrp = getMrpVer(mrpVer);
        Map<String, List<Demand>> demandMap = new HashMap<>();
        List<Demand> demandList = demandService.getDemand(mrp.getDpsVer());
        for(Demand demand : demandList) {
            demandMap.computeIfAbsent(demand.getMaterial(), k -> new ArrayList<>());
            demandMap.get(demand.getMaterial()).add(demand);
        }
        Map<String, String> materialNameMap = new HashMap<>();
        Map<String, String> materialGroupMap = new HashMap<>();
        List<Map> materialBomList = bomService.getMaterialNameAndGroup(materialList);
        for(Map map : materialBomList) {
            materialNameMap.put((String) map.get("material"), (String) map.get("materialName"));
            materialGroupMap.put((String) map.get("material"), (String) map.get("materialGroup"));
        }
        Map<String, Double> goodInventoryMap = new HashMap<>();
        Map<String, Double> dullInventoryMap = new HashMap<>();
        Date d = m.getStartDate();
        if(d.after(new java.util.Date())) d = new Date(new java.util.Date().getTime());
        List<Map> goodInventoryList = inventoryService.getGoodInventory(materialList,  d, mrp.getPlant());
        List<Map> dullInventoryList = inventoryService.getDullInventory(materialList, d, mrp.getPlant());
        for(Map map : goodInventoryList) {
            goodInventoryMap.put((String) map.get("material".toUpperCase()), ((BigDecimal) map.get("qty".toUpperCase()) ).doubleValue());
        }
        for(Map map : dullInventoryList) {
            dullInventoryMap.put((String) map.get("material".toUpperCase()), ((BigDecimal) map.get("qty".toUpperCase()) ).doubleValue());
        }

        for(String material : materialList) {
            generateMrpData(mrpVer, material, demandMap, materialNameMap, materialGroupMap, goodInventoryMap, dullInventoryMap);
        }
        log.info("END>>");
    }

    private void generateMrpData(String mrpVer, String material, Map<String, List<Demand>> demandMap,
                                 Map<String, String> materialNameMap,  Map<String, String> materialGroupMap ,
                                 Map<String, Double> goodInventoryMap,  Map<String, Double> dullInventoryMap) {
        List<Demand> demandList = demandMap.get(material);
        if(demandList== null || demandList.size()==0) return;
        List<Date> dateList = getMrpCalendarList(mrpVer);
        if(dateList==null || dateList.size()==0) return;
        // 创建MrpData
        List<MrpData> mrpDataList = new ArrayList<>();
        Map<Date, MrpData> dateMap = new HashMap<>();
        for(Date date : dateList) {
            MrpData mrpData = new MrpData();
            mrpData.setMrpVer(mrpVer);
            mrpData.setMaterial(material);
            mrpData.setFabDate(date);
            dateMap.put(date, mrpData);
            mrpDataList.add(mrpData);
        }
        // 创建MrpMaterial
        MrpMaterial mrpMaterial = new MrpMaterial();
        mrpMaterial.setMaterial(material);
        mrpMaterial.setMrpVer(mrpVer);
        mrpMaterial.setMaterialName(materialNameMap.get(material));
        mrpMaterial.setMaterialGroup(materialGroupMap.get(material));
        mrpMaterial.setLossRate(materialLossRateService.getMaterialLossRate(material));
        // 获取期初库存
        mrpMaterial.setGoodInventory(goodInventoryMap.get(material)==null ? 0 : goodInventoryMap.get(material));
        mrpMaterial.setDullInventory(dullInventoryMap.get(material)==null  ? 0 : dullInventoryMap.get(material));

        // 赋值MrpData需求量、MrpMaterial机种/厂
        for(Demand demand : demandList) {
            MrpData mrpData = dateMap.get(demand.getFabDate());
            mrpData.setDemandQty(DoubleUtil.sum(mrpData.getDemandQty(), demand.getDemandQty()));

            if(StringUtils.isEmpty(mrpMaterial.getProducts())) {
                mrpMaterial.setProducts(demand.getProduct());
            } else {
                if(!StringUtils.containsIgnoreCase(mrpMaterial.getProducts(), demand.getProduct())) {
                    mrpMaterial.setProducts(mrpMaterial.getProducts() + "/" + demand.getProduct());
                }
            }
            if(StringUtils.isEmpty(mrpMaterial.getPlant())) {
                mrpMaterial.setPlant(demand.getFab());
            } else {
                if(!StringUtils.containsIgnoreCase(mrpMaterial.getPlant(), demand.getFab())) {
                    mrpMaterial.setPlant(mrpMaterial.getPlant() + "/" + demand.getFab());
                }
            }
        }

        //赋值供应商
        List<MaterialSupplier> materialSupplierList = materialSupplierService.getMaterialSupplier(material);
        String suppler = "";
        for(MaterialSupplier materialSupplier : materialSupplierList) {
            if(StringUtils.isNotEmpty(suppler)) {
                suppler += "/";
            }
            suppler += materialSupplier.getSupplierCode();
        }
        mrpMaterial.setSupplier(suppler);

        mrpMaterialService.saveMrpMaterial(mrpMaterial);
        mrpDataRepository.saveAll(mrpDataList);
    }

    public void generateMrpData(String mrpVer, String material) {
        MrpVer m = getMrpVer(mrpVer);
        if(m == null) return;
        List<Demand> demandList = demandService.getDemandByMaterial(m.getDpsVer(), material);
        if(demandList== null || demandList.size()==0) return;
        List<Date> dateList = getMrpCalendarList(mrpVer);
        if(dateList==null || dateList.size()==0) return;
        // 创建MrpData
        List<MrpData> mrpDataList = new ArrayList<>();
        Map<Date, MrpData> dateMap = new HashMap<>();
        for(Date date : dateList) {
            MrpData mrpData = new MrpData();
            mrpData.setMrpVer(mrpVer);
            mrpData.setMaterial(material);
            mrpData.setFabDate(date);
            dateMap.put(date, mrpData);
            mrpDataList.add(mrpData);
        }
        // 创建MrpMaterial
        MrpMaterial mrpMaterial = new MrpMaterial();
        mrpMaterial.setMaterial(material);
        mrpMaterial.setMrpVer(mrpVer);
        mrpMaterial.setMaterialName(bomService.getMaterialName(material));
        mrpMaterial.setMaterialGroup(bomService.getMaterialGroup(material));
        mrpMaterial.setLossRate(materialLossRateService.getMaterialLossRate(material));

        // 获取期初库存
        Date d = m.getStartDate();
        if(d.after(new java.util.Date())) d = new Date(new java.util.Date().getTime());
        mrpMaterial.setGoodInventory(inventoryService.getGoodInventory(m.getPlant(), material, d));
        mrpMaterial.setDullInventory(inventoryService.getDullInventory(m.getPlant(), material, d));

        // 赋值MrpData需求量、MrpMaterial机种/厂
        for(Demand demand : demandList) {
            MrpData mrpData = dateMap.get(demand.getFabDate());
            mrpData.setDemandQty(DoubleUtil.sum(mrpData.getDemandQty(), demand.getDemandQty()));

            if(StringUtils.isEmpty(mrpMaterial.getProducts())) {
                mrpMaterial.setProducts(demand.getProduct());
            } else {
                if(!StringUtils.containsIgnoreCase(mrpMaterial.getProducts(), demand.getProduct())) {
                    mrpMaterial.setProducts(mrpMaterial.getProducts() + "/" + demand.getProduct());
                }
            }
            if(StringUtils.isEmpty(mrpMaterial.getPlant())) {
                mrpMaterial.setPlant(demand.getFab());
            } else {
                if(!StringUtils.containsIgnoreCase(mrpMaterial.getPlant(), demand.getFab())) {
                    mrpMaterial.setPlant(mrpMaterial.getPlant() + "/" + demand.getFab());
                }
            }
        }
        mrpMaterialService.saveMrpMaterial(mrpMaterial);
        mrpDataRepository.saveAll(mrpDataList);
    }


    @Override
    public void computeLossQty(String mrpVer) {
        log.info("STA>>计算损耗量，MRP版本"+mrpVer);
        List<String> materialList = mrpMaterialService.getMaterial(mrpVer);
        if(materialList== null || materialList.size()==0) return;
        for(String material : materialList) {
            computeLossQty(mrpVer,material );
        }
        log.info("END>>");
    }

    @Override
    public void computeLossQty(String mrpVer, String material) {
        double lossRate = materialLossRateService.getMaterialLossRate(material);
        if(lossRate == 0) return;
        List<MrpData> mrpDataList = getMrpData(mrpVer, material);
        if(mrpDataList== null || mrpDataList.size()==0) return;
        for(MrpData mrpData : mrpDataList) {
            if(mrpData.getDemandQty() == 0) continue;
            mrpData.setLossQty(DoubleUtil.multiply(mrpData.getDemandQty(), lossRate));
        }
        mrpDataRepository.saveAll(mrpDataList);
    }


    @Override
    public void computeArrivalQty(String mrpVer) {
        log.info("STA>>计算到货量，MRP版本"+mrpVer);
        List<String> materialList = mrpMaterialService.getMaterial(mrpVer);
        if(materialList== null || materialList.size()==0) return;
        for(String material : materialList) {
            computeArrivalQty(mrpVer,material );
        }
        log.info("END>>");
    }

    @Override
    public void computeArrivalQty(String mrpVer, String material) {
        MrpVer mrp = getMrpVer(mrpVer);
        List<Map> arrivalList =  supplierArrivalPlanService.getDaySupplierArrivalQty(mrp.getStartDate(), mrp.getEndDate(), mrp.getPlant(), material);
        if(arrivalList==null || arrivalList.size()==0) return;
        Map<Date, Double> arrivalMap = new HashMap<>();
        for(Map map : arrivalList) {
            arrivalMap.put((Date) map.get("fabDate"), (Double) map.get("qty"));
        }
        List<MrpData> mrpDataList = getMrpData(mrpVer, material);
        if(mrpDataList== null || mrpDataList.size()==0) return;
        for(MrpData mrpData : mrpDataList) {
            mrpData.setArrivalQty(arrivalMap.get(mrpData.getFabDate())==null ? 0 : arrivalMap.get(mrpData.getFabDate()));
        }
        mrpDataRepository.saveAll(mrpDataList);
    }

    @Override
    public void computeBalance(String mrpVer) {
        log.info("STA>>计算材料的结余量，MRP版本"+mrpVer);
        List<String> materialList = mrpMaterialService.getMaterial(mrpVer);
        if(materialList == null || materialList.size() == 0) return;
        for(String material : materialList) {
            computeBalance(mrpVer, material);
        }
        log.info("END>>");
    }

    @Override
    public void computeBalance(String mrpVer, String material) {
        MrpMaterial mrpMaterial = mrpMaterialService.getMrpMaterial(mrpVer, material);
        List<MrpData> mrpDataList = getMrpData(mrpVer, material);
        if(mrpDataList==null || mrpDataList.size()==0) return;
        Map<Date, MrpData> dateMap = new HashMap<>();
        for(MrpData mrpData : mrpDataList) {
            dateMap.put(mrpData.getFabDate(), mrpData);
        }
        List<Date> dateList = getMrpCalendarList(mrpVer);
        if(dateList == null || dateList.size() == 0) return;

        MrpVer mrp = getMrpVer(mrpVer);
        List<Map> arrivalList =  supplierArrivalPlanService.getDaySupplierArrivalQty(mrp.getStartDate(), mrp.getEndDate(), mrp.getPlant(), material);
        Map<Date, Double> arrivalMap = new HashMap<>();
        if(arrivalList!=null && arrivalList.size()>0) {
            for(Map map : arrivalList) {
                arrivalMap.put((Date) map.get("fabDate"), (Double) map.get("qty"));
            }
        }

        for(int i=0; i<dateList.size(); i++) {
            Date fabDate = dateList.get(i);
            MrpData mrpData = dateMap.get(fabDate);
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
                double arrivalQty = arrivalMap.get(mrpData.getFabDate())==null ? 0 : arrivalMap.get(mrpData.getFabDate());
                mrpData.setArrivalQty(arrivalQty);
                mrpDataList.add(mrpData);
                dateMap.put(fabDate, mrpData);
            }
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                double beginInventoryQty = DoubleUtil.sum(mrpMaterial.getGoodInventory(), mrpMaterial.getDullInventory());
                double demandQty = mrpData.getDemandQty();
                double lossQty = mrpData.getLossQty();
                double balanceQty = DoubleUtil.sum(beginInventoryQty, -demandQty, -lossQty);
                mrpData.setBalanceQtyCompute(balanceQty);
                if(mrpData.getBalanceQtyModify() != 0) {
                    mrpData.setBalanceQty(mrpData.getBalanceQtyModify());
                } else {
                    mrpData.setBalanceQty(balanceQty);
                }
                if(balanceQty<0) {
                    mrpData.setShortQty(balanceQty);
                }
            } else {
                // 其他天：前一天计算的可用库存 + 前一天的到货 - 当日耗损量 – 当日需求量
                MrpData yesterdayMrpData = dateMap.get(dateList.get(i-1));
                double yesterdayBalanceQty = yesterdayMrpData.getBalanceQty();
                double yesterdayArrivalQty = yesterdayMrpData.getArrivalQty();
                double demandQty = mrpData.getDemandQty();
                double lossQty = mrpData.getLossQty();
                double balanceQty = DoubleUtil.sum(yesterdayBalanceQty, yesterdayArrivalQty, -demandQty, -lossQty);
                mrpData.setBalanceQtyCompute(balanceQty);
                if(mrpData.getBalanceQtyModify() != 0) {
                    mrpData.setBalanceQty(mrpData.getBalanceQtyModify());
                } else {
                    mrpData.setBalanceQty(balanceQty);
                }
                if(balanceQty<0) {
                    if(yesterdayBalanceQty>0) {
                        mrpData.setShortQty(balanceQty);
                    } else {
                        mrpData.setShortQty(balanceQty-yesterdayBalanceQty);
                    }
                }
            }
        }
        mrpDataRepository.saveAll(mrpDataList);
    }

    @Override
    public void updateMrpData(String mrpVer, String material, Date fabDate) {
        MrpData mrpData = getMrpData(mrpVer, material, fabDate);
        MrpVer mrpVerO = getMrpVer(mrpVer);
        mrpData.setArrivalQty(supplierArrivalPlanService.getMaterialArrivalPlanQty(mrpVerO.getPlant(), material, fabDate));
        mrpDataRepository.save(mrpData);
        computeBalance(mrpVer, material);
    }

    @Override
    public void updateBalanceQty(MrpData mrpData) {
        mrpDataRepository.save(mrpData);
    }
}
