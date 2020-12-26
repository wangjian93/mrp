package com.ivo.mrp.service.pol;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.pol.BomPol;
import com.ivo.mrp.entity.pol.DpsPol;
import com.ivo.mrp.entity.pol.MrpPol;
import com.ivo.mrp.entity.pol.MrpPolMaterial;
import com.ivo.mrp.exception.MrpException;
import com.ivo.mrp.service.*;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@Service
@Slf4j
public class RunMrpPolServiceImpl implements RunMrpPolService {


    private MrpService mrpService;

    private DpsService dpsService;

    private RunMrpService runMrpService;

    private DpsPolService dpsPolService;

    private BomPolService bomPolService;

    private LossRateService lossRateService;

    private CutService cutService;

    private MrpWarnService mrpWarnService;

    private MrpPolService mrpPolService;

    private ArrivalPlanService arrivalPlanService;

    private ActualArrivalService actualArrivalService;

    private RestService restService;

    private AllocationService allocationService;

    @Autowired
    public RunMrpPolServiceImpl(MrpService mrpService, DpsService dpsService, RunMrpService runMrpService, DpsPolService dpsPolService,
                                BomPolService bomPolService, LossRateService lossRateService, CutService cutService,
                                MrpWarnService mrpWarnService, MrpPolService mrpPolService,
                                ArrivalPlanService arrivalPlanService, ActualArrivalService actualArrivalService,
                                RestService restService, AllocationService allocationService) {
        this.mrpService = mrpService;
        this.dpsService = dpsService;
        this.runMrpService = runMrpService;
        this.dpsPolService = dpsPolService;
        this.bomPolService = bomPolService;
        this.lossRateService = lossRateService;
        this.cutService = cutService;
        this.mrpWarnService = mrpWarnService;
        this.mrpPolService = mrpPolService;
        this.arrivalPlanService = arrivalPlanService;
        this.actualArrivalService = actualArrivalService;
        this.restService = restService;
        this.allocationService = allocationService;
    }

    @Override
    public void runMrpPol(String[] dpsVers, String user) {
        MrpVer mrpVer = createMrpVer(dpsVers, user);
        computeDemandQty(mrpVer.getVer());
        computeBalanceQty(mrpVer.getVer());
        mrpVer.setMemo("更新完成");
        mrpService.saveMrpVer(mrpVer);
    }

    @Override
    public MrpVer createMrpVer(String[] dpsVers, String user) {
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
        String mrpVer = runMrpService.generateMpsVer();
        MrpVer mrp = new MrpVer();
        mrp.setVer(mrpVer);
        mrp.setDpsVer(mrpService.convertAryToString(dpsVers));
        mrp.setStartDate(startDate);
        mrp.setEndDate(endDate);
        mrp.setType(MrpVer.Type_Pol);
        mrp.setFab(fab);
        mrp.setMemo("运算中");
        mrp.setCreator(user);
        mrpService.saveMrpVer(mrp);
        return mrp;
    }

    @Override
    public void computeDemandQty(String ver) {
        String msg = "计算MRP版本"+ver+"的需求量>> ";
        log.info(msg + "START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
        // DPS
        for(String dpsVer : dpsVers) {
            computeDemandQty(ver, dpsVer);
        }
        log.info(msg + "END");
    }

    @Override
    public void computeDemandQty(String ver, String dpsVer) {
        List<String> productList = dpsPolService.getDpsPolProduct(dpsVer);
        for(String product : productList) {
            computeDemandQty(ver, dpsVer, product);
        }
    }

    @Override
    public void computeDemandQty(String ver, String dpsVer, String product) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = "CELL";
        java.sql.Date startDate = mrpVer.getStartDate();
        List<DpsPol> dpsPolList = dpsPolService.getDpsPol(dpsVer, product);
        Map<Date, Double> demandMap = new HashMap<>();
        for(DpsPol dpsPol : dpsPolList) {
            demandMap.put(dpsPol.getFabDate(), dpsPol.getDemandQty());
        }

        List<BomPol> bomPolList = bomPolService.getBomPol(product);
        List<String> materialList = new ArrayList<>();
        for(BomPol bomPol : bomPolList) {
            materialList.add(bomPol.getMaterial());
        }

        //期初库存
        java.sql.Date inventoryDate = new java.sql.Date(DateUtil.getFirstDayOfMonth(startDate).getTime());
        List<Map> goodInventoryList = restService.getGoodInventory(fab, materialList, inventoryDate);
        List<Map> dullInventoryList = restService.getDullInventory(fab, materialList, inventoryDate);
        HashMap<String, Double> goodInventoryMap = new HashMap<>();
        HashMap<String, Double> dullInventoryMap = new HashMap<>();
        for(Map map : goodInventoryList) {
            goodInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        for(Map map : dullInventoryList) {
            dullInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }

        List<String> typeList = new ArrayList<>();
        List<MrpPolMaterial> mrpPolMaterialList = new ArrayList<>();
        List<MrpPol> mrpPolList = new ArrayList<>();
        List<Date> dateList = mrpService.getMrpCalendar(ver);

        //切片数
        String project = StringUtils.contains(product, " ") ? product.substring(0, product.indexOf(" ")) : product;
        project = StringUtils.contains(project, "-") ? project.substring(0, project.indexOf("-")) : project;
        Double cut = cutService.getProjectCut(project);
        if(cut == null) {
            log.warn("警告：MPS机种"+product+"没有切片数，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "DPS", "没有切片数");
        }

        for(BomPol bomPol : bomPolList) {
            MrpPolMaterial mrpPolMaterial = new MrpPolMaterial();
            mrpPolMaterial.setVer(ver);
            mrpPolMaterial.setProduct(product);
            mrpPolMaterial.setMaterial(bomPol.getMaterial());
            mrpPolMaterial.setMaterialName(bomPol.getMaterialName());
            mrpPolMaterial.setMaterialGroup(bomPol.getMaterialGroup());
            mrpPolMaterial.setSupplierCode(bomPol.getSupplierCode());
            mrpPolMaterial.setSupplierName(bomPol.getSupplierName());
            mrpPolMaterial.setType(bomPol.getType());

            //损耗率
            double lossRate = lossRateService.getLossRate(bomPol.getMaterial());
            mrpPolMaterial.setLossRate(lossRate);


            mrpPolMaterial.setCut(cut);

            Double goodInventoryQty = goodInventoryMap.get(bomPol.getMaterial());
            Double dullInventoryQty = dullInventoryMap.get(bomPol.getMaterial());
            if(goodInventoryQty == null) goodInventoryQty =0D;
            if(dullInventoryQty == null) dullInventoryQty =0D;
            mrpPolMaterial.setGoodInventory(goodInventoryQty);
            mrpPolMaterial.setDullInventory(dullInventoryQty);
            mrpPolMaterial.setInventoryDate(inventoryDate);

            mrpPolMaterialList.add(mrpPolMaterial);

            for(Date fabDate : dateList) {
                MrpPol mrpPol = new MrpPol();
                mrpPol.setVer(ver);
                mrpPol.setProduct(product);
                mrpPol.setMaterial(bomPol.getMaterial());
                mrpPol.setFabDate(fabDate);
                mrpPolList.add(mrpPol);

                if(typeList.contains(bomPol.getType()) || demandMap.get(fabDate)==null || demandMap.get(fabDate)==0 || cut==null) continue;
                double demandQty = DoubleUtil.upPrecision(demandMap.get(fabDate)*1000*cut*(1+lossRate), 0);
                mrpPol.setDemandQty(demandQty);
            }
            typeList.add(bomPol.getType());
        }

        mrpPolService.saveMrpPolMaterial(mrpPolMaterialList);
        mrpPolService.saveMrpPol(mrpPolList);
    }

    @Override
    public void computeBalanceQty(String ver) {
        List<MrpPolMaterial> mrpPolMaterialList = mrpPolService.getMrpPolMaterial(ver);
        for(MrpPolMaterial mrpPolMaterial : mrpPolMaterialList) {
            computeBalanceQty(ver, mrpPolMaterial.getProduct(), mrpPolMaterial.getMaterial());
        }
    }

    @Override
    public void computeBalanceQty(String ver, String product, String material) {
        MrpPolMaterial mrpPolMaterial = mrpPolService.getMrpPolMaterial(ver, product, material);
        List<MrpPol> mrpPolList = mrpPolService.getMrpPol(ver, product, material);
        List<java.sql.Date> dateList = mrpService.getMrpCalendar(ver);
        String fab = "CELL";
        //到货量
        Map<java.sql.Date, Double> arrivalQtyMap = arrivalPlanService.getArrivalPlanQty(fab, material, dateList);
        //分配量
        Map<java.sql.Date, Double> allocationQtyMap = allocationService.getAllocationQty(fab, material, dateList);

        //期初库存
        double goodInventory = mrpPolMaterial.getGoodInventory();
        for(int i=0; i<mrpPolList.size(); i++) {
            MrpPol mrpPol = mrpPolList.get(i);
            java.sql.Date fabDate = mrpPol.getFabDate();
            //需求量
            double demandQty = mrpPol.getDemandQty();

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
                MrpPol lastMrpPol = mrpPolList.get(i-1);
                double lastBalanceQty = lastMrpPol.getBalanceQty();
                double lastArrivalQty = lastMrpPol.getArrivalQty();
                balanceQty =  DoubleUtil.upPrecision(lastBalanceQty + lastArrivalQty - demandQty, 0);
            }
            //判断结余量是否修改
            if(mrpPol.isModifyBalanceFlag()) {
                mrpPol.setBalanceQtyHis(balanceQty);
                balanceQty = mrpPol.getBalanceQty();
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
                    MrpPol lastMrpPol = mrpPolList.get(i-1);
                    double lastBalanceQty = lastMrpPol.getBalanceQty();
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

            mrpPol.setArrivalQty(arrivalQty);
            mrpPol.setBalanceQty(balanceQty);
            mrpPol.setShortQty(shortQty);
            mrpPol.setAllocationQty(allocationQty);
            mrpPol.setArrivalActualQty(arrivalActualQty);
            mrpPol.setArrivalPlanQty(arrivalPlanQty);
        }
        mrpPolService.saveMrpPol(mrpPolList);
    }
}
