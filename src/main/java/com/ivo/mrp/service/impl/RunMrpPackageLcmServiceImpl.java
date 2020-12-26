package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.MpsVer;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.Supplier;
import com.ivo.mrp.entity.direct.lcm.*;
import com.ivo.mrp.entity.lcmPackaging.BomPackagingLcm;
import com.ivo.mrp.entity.lcmPackaging.DemandPackageLcm;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcm;
import com.ivo.mrp.entity.lcmPackaging.MrpPackageLcmMaterial;
import com.ivo.mrp.exception.MrpException;
import com.ivo.mrp.service.*;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class RunMrpPackageLcmServiceImpl implements RunMrpPackageLcmService {

    private DpsService dpsService;
    private MpsService mpsService;
    private MrpService mrpService;
    private RunMrpService runMrpService;
    private BomPackageLcmService bomPackageLcmService;
    private MrpWarnService mrpWarnService;
    private SubstituteService substituteService;
    private DemandPackageLcmService demandPackageLcmService;
    private RestService restService;
    private LossRateService lossRateService;
    private MrpPackageLcmService mrpPackageLcmService;
    private ActualArrivalService actualArrivalService;
    private MaterialService materialService;
    private MaterialGroupService materialGroupService;
    private SupplierService supplierService;

    public RunMrpPackageLcmServiceImpl(DpsService dpsService, MpsService mpsService,
                                       MrpService mrpService, RunMrpService runMrpService,
                                       BomPackageLcmService bomPackageLcmService, MrpWarnService mrpWarnService,
                                       SubstituteService substituteService,
                                       DemandPackageLcmService demandPackageLcmService,
                                       RestService restService, LossRateService lossRateService,
                                       MrpPackageLcmService mrpPackageLcmService,
                                       ActualArrivalService actualArrivalService,
                                       MaterialService materialService,
                                       MaterialGroupService materialGroupService,
                                       SupplierService supplierService) {
        this.dpsService = dpsService;
        this.mpsService = mpsService;
        this.mrpService = mrpService;
        this.runMrpService = runMrpService;
        this.bomPackageLcmService = bomPackageLcmService;
        this.mrpWarnService = mrpWarnService;
        this.substituteService = substituteService;
        this.demandPackageLcmService = demandPackageLcmService;
        this.restService = restService;
        this.lossRateService = lossRateService;
        this.mrpPackageLcmService = mrpPackageLcmService;
        this.actualArrivalService = actualArrivalService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.supplierService = supplierService;
    }

    @Override
    public MrpVer createMrpVer(String[] dpsVers, String[] mpsVers, String user) {
        if(mpsVers==null) mpsVers = new String[0];
        Date startDate = null;
        Date endDate = null;
        String fab = null;
        for(String dpsVer : dpsVers) {
            DpsVer dps = dpsService.getDpsVer(dpsVer);
            if(dps==null) {
                throw new MrpException("DPS版本"+dpsVer+"不存在");
            }
            fab = dps.getFab();
            if(startDate == null) {
                startDate = dps.getStartDate();
            } else {
                if(dps.getStartDate().before(startDate)) startDate = dps.getStartDate();
            }
            if(endDate == null) {
                endDate = dps.getEndDate();
            } else {
                if(dps.getEndDate().after(endDate)) endDate = dps.getEndDate();
            }
        }
        for(String mpsVer : mpsVers) {
            MpsVer mps = mpsService.getMpsVer(mpsVer);
            if(mps==null) {
                throw new MrpException("MPS版本"+mpsVer+"不存在");
            }
            if(startDate == null) {
                startDate = mps.getStartDate();
            } else {
                if(mps.getStartDate().before(startDate)) startDate = mps.getStartDate();
            }
            if(endDate == null) {
                endDate = mps.getEndDate();
            } else {
                if(mps.getEndDate().after(endDate)) endDate = mps.getEndDate();
            }
        }


        String mrpVer = runMrpService.generateMpsVer();

        MrpVer mrp = new MrpVer();
        mrp.setVer(mrpVer);
        mrp.setDpsVer(mrpService.convertAryToString(dpsVers));
        mrp.setMpsVer(mrpService.convertAryToString(mpsVers));
        mrp.setStartDate(startDate);
        mrp.setEndDate(endDate);
        mrp.setType(MrpVer.Type_Package_LCM);
        mrp.setFab(fab);
        mrp.setMemo("运算中");
        mrp.setCreator(user);

        mrpService.saveMrpVer(mrp);
        return mrp;
    }

    @Override
    public void computeDemand(String ver) {
        String msg = "计算MRP版本"+ver+"的需求量>> ";
        log.info(msg + "START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
        String[] mpsVers = mrpService.convertStringToAry(mrpVer.getMpsVer());
        // DPS
        for(String dpsVer : dpsVers) {
            computeDpsDemand(ver, dpsVer);
        }
        // MPS
        for(String mpsVer : mpsVers) {
            computeMpsDemand(ver, mpsVer);
        }
        log.info(msg + "END");
    }

    @Override
    public void computeDpsDemand(String ver, String dpsVer) {
        String msg = "计算MRP版本"+ver+"的需求量>> DPS" + dpsVer ;
        log.info(msg);
        List<String> productList = dpsService.getDpsLcmProduct(dpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeDpsDemand(ver, dpsVer, product);
        }
    }

    @Override
    public void computeDpsDemand(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = dps.getFab();
        List<BomPackagingLcm> bomLcmList = bomPackageLcmService.getLcmPackageBom(fab, product);
        if(bomLcmList == null || bomLcmList.size()==0) {
            log.warn("警告：DPS机种"+product+"没有BOM List，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "DPS", "没有BOM");
            return;
        }
        List<DpsLcm> dpsLcmList = dpsService.getDpsLcm(dpsVer, product, mrpVer.getStartDate());
        List<DemandPackageLcm> demandLcmList = new ArrayList<>();
        for(DpsLcm dpsLcm : dpsLcmList) {
            for(BomPackagingLcm bomLcm : bomLcmList) {
                DemandPackageLcm demandPackageLcm = new DemandPackageLcm();
                demandPackageLcm.setVer(ver);
                demandPackageLcm.setType(DemandPackageLcm.TYPE_DPS);
                demandPackageLcm.setDpsMpsVer(dpsVer);
                demandPackageLcm.setFab(fab);
                demandPackageLcm.setProduct(dpsLcm.getProduct());
                demandPackageLcm.setProject(dpsLcm.getProject());
                demandPackageLcm.setFabDate(dpsLcm.getFabDate());
                demandPackageLcm.setQty(dpsLcm.getDemandQty());
                demandPackageLcm.setMaterial(bomLcm.getMaterial());
                demandPackageLcm.setUsageQty(bomLcm.getUsageQty());
                //替代料比例
                Double substituteRate = substituteService.getSubstituteRate(bomLcm.getFab(), bomLcm.getProduct(), bomLcm.getMaterialGroup(), bomLcm.getMaterial());
                demandPackageLcm.setSubstituteRate(substituteRate);

                //DPS需求量 * Bon使用量 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandPackageLcm.getQty()*demandPackageLcm.getUsageQty()*(substituteRate/100), 0);
                demandPackageLcm.setDemandQty(demandQty);

                demandPackageLcm.setAlone(bomLcm.isAlongFlag());
                demandLcmList.add(demandPackageLcm);
            }
        }
        demandPackageLcmService.saveDemandPackageLcm(demandLcmList);
    }

    @Override
    public void computeMpsDemand(String ver, String mpsVer) {
        String msg = "计算MRP版本"+ver+"的需求量>> MPS" + mpsVer ;
        log.info(msg);
        List<String> productList = mpsService.getMpsLcmProduct(mpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeMpsDemand(ver, mpsVer, product);
        }
    }

    @Override
    public void computeMpsDemand(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String fab = mps.getFab();
        List<BomPackagingLcm> bomLcmList = bomPackageLcmService.getLcmPackageBom(fab, product);
        if(bomLcmList == null || bomLcmList.size()==0) {
            log.warn("警告：MPS机种"+product+"没有BOM List，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "MPS", "没有BOM");
            return;
        }

        //MPS计算从DPS结束后开始
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
        java.sql.Date dps_endDate = mrpVer.getStartDate();
        for(String dpsVer : dpsVers) {
            java.sql.Date date = dpsService.getDpsVer(dpsVer).getEndDate();
            if(dps_endDate.before(date)) {
                dps_endDate = date;
            }
        }
        java.sql.Date startDate = dps_endDate;

        List<MpsLcm> mpsLcmList = mpsService.getMpsLcm(mpsVer, product, startDate);
        List<DemandPackageLcm> demandLcmList = new ArrayList<>();
        for(MpsLcm mpsLcm : mpsLcmList) {
            for(BomPackagingLcm bomLcm : bomLcmList) {
                DemandPackageLcm demandLcm = new DemandPackageLcm();
                demandLcm.setVer(ver);
                demandLcm.setType(DemandLcm.TYPE_MPS);
                demandLcm.setDpsMpsVer(mpsVer);
                demandLcm.setFab(fab);
                demandLcm.setProduct(mpsLcm.getProduct());
                demandLcm.setProject(mpsLcm.getProject());
                demandLcm.setFabDate(mpsLcm.getFabDate());
                demandLcm.setQty(mpsLcm.getDemandQty());
                demandLcm.setMaterial(bomLcm.getMaterial());
                demandLcm.setUsageQty(bomLcm.getUsageQty());
                //替代料比例
                Double substituteRate = substituteService.getSubstituteRate(bomLcm.getFab(), bomLcm.getProduct(), bomLcm.getMaterialGroup(), bomLcm.getMaterial());
                demandLcm.setSubstituteRate(substituteRate);

                //MPS需求量 * Bon使用量 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandLcm.getQty()*demandLcm.getUsageQty()*(substituteRate/100), 0);
                demandLcm.setDemandQty(demandQty);

                demandLcm.setAlone(bomLcm.isAlongFlag());
                demandLcmList.add(demandLcm);
            }
        }
        demandPackageLcmService.saveDemandPackageLcm(demandLcmList);
    }

    @Override
    public void computeMrpMaterial(String ver) {
        String msg = "计算MRP版本"+ver+"的材料损耗率、期初库存>> ";
        log.info(msg + "START");

        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        java.sql.Date startDate = mrpVer.getStartDate();
        List<String> materialList = demandPackageLcmService.getDemandAloneMaterial(ver);
        //共用料
        //期初库存
        java.sql.Date inventoryDate = new java.sql.Date(DateUtil.getFirstDayOfMonth(startDate).getTime());
        List<Map> goodInventoryList = restService.getGoodInventory(fab, materialList, inventoryDate);
        List<Map> dullInventoryList = restService.getDullInventory(fab, materialList, inventoryDate);
        HashMap<String, Double> goodInventoryMap = new HashMap<>();
        HashMap<String, Double> dullInventoryMap = new HashMap<>();
        List<MrpPackageLcmMaterial> mrpLcmMaterialList = new ArrayList<>();
        for(Map map : goodInventoryList) {
            goodInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        for(Map map : dullInventoryList) {
            dullInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        for(String material : materialList) {
            MrpPackageLcmMaterial mrpLcmMaterial = new MrpPackageLcmMaterial();
            mrpLcmMaterial.setVer(ver);
            mrpLcmMaterial.setFab(fab);
            mrpLcmMaterial.setMaterial(material);
            //损耗率
            double lossRate = lossRateService.getLossRate(material);
            mrpLcmMaterial.setLossRate(lossRate);

            //期初库存
            Double goodInventory = goodInventoryMap.get(material);
            Double dullInventory = dullInventoryMap.get(material);
            mrpLcmMaterial.setGoodInventory(goodInventory == null ? 0 : goodInventory);
            mrpLcmMaterial.setDullInventory(dullInventory == null ? 0 : dullInventory);
            mrpLcmMaterial.setInventorDate(inventoryDate);

            mrpLcmMaterial.setAlone(true);
            mrpLcmMaterialList.add(mrpLcmMaterial);
        }

        //套材
        List<Map> productMaterialList = demandPackageLcmService.getDemandProductMaterial(ver);
        for(Map productMaterial : productMaterialList) {
            String material = (String) productMaterial.get("material");
            String product = (String) productMaterial.get("product");

            MrpPackageLcmMaterial mrpLcmMaterial = new MrpPackageLcmMaterial();
            mrpLcmMaterial.setVer(ver);
            mrpLcmMaterial.setFab(fab);
            mrpLcmMaterial.setProducts(product);
            mrpLcmMaterial.setMaterial(material);
            //损耗率
            double lossRate = lossRateService.getLossRate(material);
            mrpLcmMaterial.setLossRate(lossRate);

            //期初库存
            mrpLcmMaterial.setGoodInventory(0d);
            mrpLcmMaterial.setDullInventory(0d);
            mrpLcmMaterial.setInventorDate(inventoryDate);

            mrpLcmMaterial.setAlone(false);
            mrpLcmMaterialList.add(mrpLcmMaterial);
        }
        mrpPackageLcmService.saveMrpPackageLcmMaterial(mrpLcmMaterialList);
        log.info(msg + "END");
    }

    @Override
    public void computeMrpBalance(String ver) {
        computeMrpBalanceAloneMaterial(ver);
        computeMrpBalanceProductMaterial(ver);
    }

    @Override
    public void computeMrpBalanceAloneMaterial(String ver) {
        List<MrpPackageLcmMaterial> mrpPackageLcmMaterialList = mrpPackageLcmService.getMrpPackageLcmAloneMaterial(ver);
        long l = mrpPackageLcmMaterialList.size();
        for(MrpPackageLcmMaterial mrpPackageLcmMaterial : mrpPackageLcmMaterialList) {
            log.info("计算共用料余结量料号" + mrpPackageLcmMaterial.getMaterial() + ",剩余" + l--);
            computeMrpBalanceAloneMaterial(ver, mrpPackageLcmMaterial);
        }
    }

    @Override
    public void computeMrpBalanceAloneMaterial(String ver, MrpPackageLcmMaterial mrpPackageLcmMaterial) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        String material = mrpPackageLcmMaterial.getMaterial();
        //损耗率
        double lossRate = mrpPackageLcmMaterial.getLossRate();
        //期初库存
        double goodInventory = mrpPackageLcmMaterial.getGoodInventory();
        //每日MRP
        List<MrpPackageLcm> mrpPackageLcmList = mrpPackageLcmService.getMrpPackageLcmAlone(ver, material);

        List<java.sql.Date> dateList = mrpService.getMrpCalendar(ver);
        if(mrpPackageLcmList == null || mrpPackageLcmList.size()==0) {
            mrpPackageLcmList = new ArrayList<>();
            for(java.sql.Date fabDate : dateList) {
                MrpPackageLcm mrpPackageLcm = new MrpPackageLcm();
                mrpPackageLcm.setFab(fab);
                mrpPackageLcm.setVer(ver);
                mrpPackageLcm.setMaterial(material);
                mrpPackageLcm.setFabDate(fabDate);
                mrpPackageLcmList.add(mrpPackageLcm);
            }
        }
        //需求量
        Map<java.sql.Date, Double> demandQtyMap = demandPackageLcmService.getDemandQtyAlone(ver, material);
        //到货量
        Map<java.sql.Date, Double> arrivalQtyMap = new HashMap<>();//arrivalPlanService.getArrivalPlanQty(fab, material, dateList);
        //分配量
        Map<java.sql.Date, Double> allocationQtyMap = new HashMap<>();//allocationService.getAllocationQty(fab, material, dateList);

        for(int i = 0; i<mrpPackageLcmList.size(); i++) {
            MrpPackageLcm mrpPackageLcm = mrpPackageLcmList.get(i);
            java.sql.Date fabDate = mrpPackageLcm.getFabDate();

            //需求量
            double demandQty;
            if(demandQtyMap.get(fabDate) == null) {
                demandQty = 0;
            } else {
                demandQty = demandQtyMap.get(fabDate);
            }
            //当日耗损量
//            double lossQty = DoubleUtil.upPrecision(demandQty*lossRate/100, 0);

            // 计划到货量
            double arrivalPlanQty;
            if(arrivalQtyMap.get(fabDate) == null) {
                arrivalPlanQty = 0;
            } else {
                arrivalPlanQty = arrivalQtyMap.get(fabDate);
            }
            //实际收货量
            double arrivalActualQty = 0;
            //供应商到货量
            double arrivalQty;
            //当天之前按实际收货，之后按计划收货
            if(fabDate.before(new java.sql.Date(new java.util.Date().getTime()))) {
                arrivalActualQty = actualArrivalService.getActualArrivalQty(fabDate, material, fab);
                arrivalQty = arrivalActualQty;
            } else {
                arrivalQty = arrivalPlanQty;
            }

            // 结余量
            double balanceQty;
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                balanceQty = DoubleUtil.upPrecision(goodInventory-demandQty, 0);

            } else {
                // 其他天：前一天的结余量 + 前一天的到货 – 当日需求量 - 当日耗损量
                //前一天结余、到货
                MrpPackageLcm lastMrpPackageLcm = mrpPackageLcmList.get(i-1);
                double lastBalanceQty = lastMrpPackageLcm.getBalanceQty();
                double lastArrivalQty = lastMrpPackageLcm.getArrivalQty();
                balanceQty =  DoubleUtil.upPrecision(lastBalanceQty + lastArrivalQty - demandQty, 0);
            }
            //判断结余量是否修改
            if(mrpPackageLcm.isModifyBalanceFlag()) {
                mrpPackageLcm.setBalanceQtyHis(balanceQty);
                balanceQty = mrpPackageLcm.getBalanceQty();
            }

            //缺料量
            double shortQty;
            if(i==0) {
                if(balanceQty<0) {
                    shortQty = balanceQty;
                } else {
                    shortQty = 0;
                }
            } else {
                if(balanceQty<0) {
                    MrpPackageLcm lastMrpPackageLcm = mrpPackageLcmList.get(i-1);
                    double lastBalanceQty = lastMrpPackageLcm.getBalanceQty();
                    if(lastBalanceQty>0) {
                        shortQty = balanceQty;
                    } else {
                        shortQty = DoubleUtil.upPrecision(balanceQty - lastBalanceQty, 0);
                        shortQty = shortQty > 0 ? 0 : shortQty;
                    }
                } else {
                    shortQty = 0;
                }
            }

            //分配量
            double allocationQty;
            if(allocationQtyMap.get(fabDate) == null) {
                allocationQty = 0;
            } else {
                allocationQty = allocationQtyMap.get(fabDate);
            }

            mrpPackageLcm.setDemandQty(demandQty);
//            mrpPackageLcm.setLossQty(lossQty);
            mrpPackageLcm.setArrivalQty(arrivalQty);
            mrpPackageLcm.setBalanceQty(balanceQty);
            mrpPackageLcm.setShortQty(shortQty);
            mrpPackageLcm.setAllocationQty(allocationQty);
            mrpPackageLcm.setArrivalActualQty(arrivalActualQty);
            mrpPackageLcm.setArrivalPlanQty(arrivalPlanQty);

        }
        mrpPackageLcmService.saveMrpPackageLcm(mrpPackageLcmList);
    }

    @Override
    public void computeMrpBalanceProductMaterial(String ver) {
        List<MrpPackageLcmMaterial> mrpPackageLcmMaterialList = mrpPackageLcmService.getMrpPackageLcmProductMaterial(ver);
        long l = mrpPackageLcmMaterialList.size();
        for(MrpPackageLcmMaterial mrpPackageLcmMaterial : mrpPackageLcmMaterialList) {
            log.info("计算套料余结量料号" + mrpPackageLcmMaterial.getMaterial() + ",剩余" + l--);
            computeMrpBalanceProductMaterial(ver, mrpPackageLcmMaterial);
        }
    }

    @Override
    public void computeMrpBalanceProductMaterial(String ver, MrpPackageLcmMaterial mrpPackageLcmMaterial) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        String material = mrpPackageLcmMaterial.getMaterial();
        String product = mrpPackageLcmMaterial.getProducts();
        //损耗率
        double lossRate = mrpPackageLcmMaterial.getLossRate();
        //期初库存
        double goodInventory = mrpPackageLcmMaterial.getGoodInventory();
        //每日MRP
        List<MrpPackageLcm> mrpPackageLcmList = mrpPackageLcmService.getMrpPackageLcmProduct(ver, product, material);

        List<java.sql.Date> dateList = mrpService.getMrpCalendar(ver);
        if(mrpPackageLcmList == null || mrpPackageLcmList.size()==0) {
            mrpPackageLcmList = new ArrayList<>();
            for(java.sql.Date fabDate : dateList) {
                MrpPackageLcm mrpPackageLcm = new MrpPackageLcm();
                mrpPackageLcm.setFab(fab);
                mrpPackageLcm.setVer(ver);
                mrpPackageLcm.setProduct(product);
                mrpPackageLcm.setMaterial(material);
                mrpPackageLcm.setFabDate(fabDate);
                mrpPackageLcmList.add(mrpPackageLcm);
            }
        }
        //需求量
        Map<java.sql.Date, Double> demandQtyMap = demandPackageLcmService.getDemandQtyProduct(ver, product, material);
        //到货量
        Map<java.sql.Date, Double> arrivalQtyMap = new HashMap<>();//arrivalPlanService.getArrivalPlanQty(fab, material, dateList);
        //分配量
        Map<java.sql.Date, Double> allocationQtyMap = new HashMap<>();//allocationService.getAllocationQty(fab, material, dateList);

        for(int i = 0; i<mrpPackageLcmList.size(); i++) {
            MrpPackageLcm mrpPackageLcm = mrpPackageLcmList.get(i);
            java.sql.Date fabDate = mrpPackageLcm.getFabDate();

            //需求量
            double demandQty;
            if(demandQtyMap.get(fabDate) == null) {
                demandQty = 0;
            } else {
                demandQty = demandQtyMap.get(fabDate);
            }
            //当日耗损量
//            double lossQty = DoubleUtil.upPrecision(demandQty*lossRate/100, 0);

            // 计划到货量
            double arrivalPlanQty;
            if(arrivalQtyMap.get(fabDate) == null) {
                arrivalPlanQty = 0;
            } else {
                arrivalPlanQty = arrivalQtyMap.get(fabDate);
            }
            //实际收货量
            double arrivalActualQty = 0;
            //供应商到货量
            double arrivalQty;
            //当天之前按实际收货，之后按计划收货
            if(fabDate.before(new java.sql.Date(new java.util.Date().getTime()))) {
                arrivalActualQty = actualArrivalService.getActualArrivalQty(fabDate, material, fab);
                arrivalQty = arrivalActualQty;
            } else {
                arrivalQty = arrivalPlanQty;
            }

            // 结余量
            double balanceQty;
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                balanceQty = DoubleUtil.upPrecision(goodInventory-demandQty, 0);

            } else {
                // 其他天：前一天的结余量 + 前一天的到货 – 当日需求量 - 当日耗损量
                //前一天结余、到货
                MrpPackageLcm lastMrpPackageLcm = mrpPackageLcmList.get(i-1);
                double lastBalanceQty = lastMrpPackageLcm.getBalanceQty();
                double lastArrivalQty = lastMrpPackageLcm.getArrivalQty();
                balanceQty =  DoubleUtil.upPrecision(lastBalanceQty + lastArrivalQty - demandQty, 0);
            }
            //判断结余量是否修改
            if(mrpPackageLcm.isModifyBalanceFlag()) {
                mrpPackageLcm.setBalanceQtyHis(balanceQty);
                balanceQty = mrpPackageLcm.getBalanceQty();
            }

            //缺料量
            double shortQty;
            if(i==0) {
                if(balanceQty<0) {
                    shortQty = balanceQty;
                } else {
                    shortQty = 0;
                }
            } else {
                if(balanceQty<0) {
                    MrpPackageLcm lastMrpPackageLcm = mrpPackageLcmList.get(i-1);
                    double lastBalanceQty = lastMrpPackageLcm.getBalanceQty();
                    if(lastBalanceQty>0) {
                        shortQty = balanceQty;
                    } else {
                        shortQty = DoubleUtil.upPrecision(balanceQty - lastBalanceQty, 0);
                        shortQty = shortQty > 0 ? 0 : shortQty;
                    }
                } else {
                    shortQty = 0;
                }
            }

            //分配量
            double allocationQty;
            if(allocationQtyMap.get(fabDate) == null) {
                allocationQty = 0;
            } else {
                allocationQty = allocationQtyMap.get(fabDate);
            }

            mrpPackageLcm.setDemandQty(demandQty);
//            mrpPackageLcm.setLossQty(lossQty);
            mrpPackageLcm.setArrivalQty(arrivalQty);
            mrpPackageLcm.setBalanceQty(balanceQty);
            mrpPackageLcm.setShortQty(shortQty);
            mrpPackageLcm.setAllocationQty(allocationQty);
            mrpPackageLcm.setArrivalActualQty(arrivalActualQty);
            mrpPackageLcm.setArrivalPlanQty(arrivalPlanQty);

        }
        mrpPackageLcmService.saveMrpPackageLcm(mrpPackageLcmList);
    }

    @Override
    public void completeMrpMaterial(String ver) {
        List<MrpPackageLcmMaterial> materialList = mrpPackageLcmService.getMrpPackageLcmMaterial(ver);
        for(MrpPackageLcmMaterial mrpPackageLcmMaterial : materialList) {
            String material = mrpPackageLcmMaterial.getMaterial();
            String materialName = materialService.getMaterialName(material);
            String materialGroup = materialService.getMaterialGroup(material);
            String materialGroupName = materialGroupService.getMaterialGroupName(materialGroup);

            List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
            StringBuffer supplierCodeStr = new StringBuffer();
            StringBuffer supplierStr = new StringBuffer();
            if(supplierList != null) {
                for(Supplier supplier : supplierList) {
                    if(StringUtils.isNotEmpty(supplierCodeStr)) {
                        supplierCodeStr.append(",");
                        supplierStr.append(",");
                    }
                    if(StringUtils.isNotEmpty(supplier.getSupplierCode())) {
                        supplierCodeStr.append(supplier.getSupplierCode());
                    }
                    if(StringUtils.isNotEmpty(supplier.getSupplierSname())) {
                        supplierStr.append(supplier.getSupplierSname());
                    }
                }
            }


            mrpPackageLcmMaterial.setMaterialName(materialName);
            mrpPackageLcmMaterial.setMaterialGroup(materialGroup);
            mrpPackageLcmMaterial.setMaterialGroupName(materialGroupName);
            mrpPackageLcmMaterial.setSupplierCodes(supplierCodeStr.toString());
            mrpPackageLcmMaterial.setSuppliers(supplierStr.toString());

            if(mrpPackageLcmMaterial.isAlone()) {
                List<String> productList = demandPackageLcmService.getDemandAloneMaterialProduct(ver, material);
                StringBuffer productStr = new StringBuffer();
                for(String product : productList) {
                    if(StringUtils.isNotEmpty(productStr)) {
                        productStr.append(",");
                    }
                    if(StringUtils.isNotEmpty(product)) {
                        productStr.append(product);
                    }
                }
                mrpPackageLcmMaterial.setProducts(productStr.toString());
            }
        }
        mrpPackageLcmService.saveMrpPackageLcmMaterial(materialList);
    }

    @Override
    public void runMrp(String[] dpsVers, String[] mpsVers, String user) {
        MrpVer mrpVer = createMrpVer(dpsVers, mpsVers, user);
        String ver = mrpVer.getVer();
        computeDemand(ver);
        computeMrpMaterial(ver);
        computeMrpBalance(ver);
        completeMrpMaterial(ver);
        mrpVer.setMemo("运算完成");
        mrpService.saveMrpVer(mrpVer);
    }

    @Override
    public void updateMrpPackageMaterial(String ver, String product, String material) {
        MrpPackageLcmMaterial mrpPackageLcmMaterial = mrpPackageLcmService.getMrpPackageLcmMaterial(ver, product, material);
       if(StringUtils.isEmpty(product)) {
            computeMrpBalanceAloneMaterial(ver,  mrpPackageLcmMaterial);
       } else {
           computeMrpBalanceProductMaterial(ver, mrpPackageLcmMaterial);
       }
    }

    @Override
    public void updateMrpBalanceQty(String ver, String product, String material, Date fabDate, double balanceQty) {
        MrpPackageLcm mrpLcm = mrpPackageLcmService.getMrpPackageLcm(ver, product, material, fabDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(mrpLcm.getBalanceQtyHis() == balanceQty) {
            mrpLcm.setModifyBalanceFlag(false);
            mrpLcm.setBalanceQty(balanceQty);
        } else {
            if(mrpLcm.isModifyBalanceFlag()) {
                mrpLcm.setBalanceQty(balanceQty);
                mrpLcm.setMemo("结余量修改"+sdf.format(new java.util.Date()));
            } else {
                mrpLcm.setModifyBalanceFlag(true);
                mrpLcm.setBalanceQtyHis(mrpLcm.getBalanceQty());
                mrpLcm.setBalanceQty(balanceQty);
                mrpLcm.setMemo("结余量修改"+sdf.format(new java.util.Date()));
            }
        }

        List<MrpPackageLcm> list = new ArrayList<>();
        list.add(mrpLcm);
        mrpPackageLcmService.saveMrpPackageLcm(list);
    }
}
