package com.ivo.mrp.service.packageing;

import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp.entity.DpsVer;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.packaging.*;
import com.ivo.mrp.exception.MrpException;
import com.ivo.mrp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class RunMrpPackageServiceImpl implements RunMrpPackageService {

    private MrpService mrpService;
    private DpsService dpsService;

    private DpsPackageService dpsPackageService;
    private RunMrpService runMrpService;
    private MrpPackageService mrpPackageService;
    private BomPackageService bomPackageService;
    private PackageAllocationService packageAllocationService;
    private MrpWarnService mrpWarnService;

    public RunMrpPackageServiceImpl(MrpService mrpService, DpsService dpsService, DpsPackageService dpsPackageService,
                                    RunMrpService runMrpService, MrpPackageService mrpPackageService,
                                    BomPackageService bomPackageService,
                                    PackageAllocationService packageAllocationService,
                                    MrpWarnService mrpWarnService) {
        this.mrpService = mrpService;
        this.dpsService = dpsService;
        this.dpsPackageService = dpsPackageService;
        this.runMrpService = runMrpService;
        this.mrpPackageService = mrpPackageService;
        this.bomPackageService = bomPackageService;
        this.packageAllocationService = packageAllocationService;
        this.mrpWarnService = mrpWarnService;
    }

    //    public RunMrpPackageServiceImpl(RunMrpService runMrpService, MrpService mrpService, BomPackageService bomPackageService,
//                                    DpsService dpsService, AllocationService allocationService,
//                                    ArrivalPlanService arrivalPlanService) {
//        this.runMrpService = runMrpService;
//        this.mrpService = mrpService;
//        this.bomPackageService = bomPackageService;
//        this.dpsService = dpsService;
//        this.allocationService = allocationService;
//        this.arrivalPlanService = arrivalPlanService;
//    }
//
//    @Override
//    public void runMrp(String[] dpsVers, String[] mpsVers, String user) {
//        MrpVer mrpVer = runMrpService.createMrpVer(dpsVers, mpsVers, user);
//        String type = mrpVer.getType();
//        String ver = mrpVer.getVer();
//        if(type.equals(MrpVer.Type_Package)) {
//            computeMrpPackage(ver);
//        }
//    }
//
//    @Override
//    public void computeMrpPackage(String ver) {
//        log.info("计算MRP版本" + ver +"的需求>> START");
//        MrpVer mrpVer = mrpService.getMrpVer(ver);
//        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
//        for(String dpsVer : dpsVers) {
//            computeMrpPackage(ver, dpsVer);
//        }
//        log.info("计算MRP版本" + ver +"的需求>> END");
//    }
//
//    @Override
//    public void computeMrpPackage(String ver, String dpsVer) {
//        log.info("计算MRP版本"+ver+"的需求量>> DPS" + dpsVer);
//        List<Map> mapList = dpsService.getDpsPackageProduct(dpsVer);
//        int l = mapList.size();
//        for(Map map : mapList) {
//            String product = (String) map.get("product");
//            String type = (String) map.get("type");
//            Double linkQty = (Double) map.get("linkQty");
//            String mode = (String) map.get("mode");
//            log.info("计算机种"+product+" "+type+",剩余"+l--);
//            computeMrpPackage(ver, dpsVer, product, type, linkQty, mode);
//        }
//    }
//
//    @Override
//    public void computeMrpPackage(String ver, String dpsVer, String product, String type, Double linkQty, String mode) {
//        //BOM纸箱、tray盘
//        BomPackage bomPackage = bomPackageService.getBomPackage(product, type, linkQty, mode);
//        if(bomPackage == null) {
//            log.warn("警告：包材机种"+product+type+"没有BOM List，MRP版本"+ver);
//            return;
//        }
//        BomPackageMaterial box = null;
//        BomPackageMaterial tray = null;
//        for(BomPackageMaterial bomPackageMaterial : bomPackage.getMaterialList()) {
//            if(bomPackageMaterial.isSupplierFlag()) {
//                if(StringUtils.containsIgnoreCase(bomPackageMaterial.getMaterialName(), "Tray")) {
//                    tray = bomPackageMaterial;
//                } else if(StringUtils.contains(bomPackageMaterial.getMaterialName(), "箱")) {
//                    box = bomPackageMaterial;
//                } else {
//                    tray = bomPackageMaterial;
//                }
//            }
//        }
//        if(box == null || tray == null) {
//            log.warn("警告：包材机种"+product+type+"的BOM List中没有纸箱或Tray盘，MRP版本"+ver);
//            return;
//        }
//
//        //需求量
//        List<DpsPackage> dpsPackageList = dpsService.getDpsPackage(dpsVer, product, type, linkQty, mode);
//
//        mrpService.deleteMrpPackage(mrpService.getMrpPackage(ver, product, type, linkQty, mode));
//        List<MrpPackage> mrpPackageList = new ArrayList<>();
//        for(DpsPackage dpsPackage : dpsPackageList) {
//            java.sql.Date fabDate = dpsPackage.getFabDate();
//            double demandQty = dpsPackage.getDemandQty();
//            double box_qty = 0;
//            double tray_qty = 0;
//
//            if(demandQty>0) {
//                // 单片
//                if(StringUtils.equals(type, BomPackage.TYPE_D )) {
//                    if(StringUtils.equals(mode, "全切单")) {
//                        //cellInput*1000*54切数/120Panel数目*1.1（10%损耗） (全切单)
//                        double lossRate =  box.getLossRate()==null ? 0 : box.getLossRate();
//                        double cutQty = bomPackage.getCutQty();
//                        double panelQty = bomPackage.getPanelQty();
//                        box_qty = DoubleUtil.upPrecision(demandQty*1000*cutQty/panelQty*(1+lossRate/100), 0);
//                    } else {
//                        //cellinput*1000/600*3（抽单模式）
//                        String[] modeS = mode.split("抽");
//                        double mode_1 = Double.valueOf(modeS[0]);
//                        double mode_2 = Double.valueOf(modeS[1]);
//                        box_qty =  DoubleUtil.upPrecision(demandQty*1000/mode_1*mode_2, 0);
//                    }
//                } else {
//                    //连片
//                    //cellinput*连片数40*中板数3*单耗量G*1000*1.05（5%损耗）
//                    double consumeQt = box.getConsumeQty();
//                    double middleQty = bomPackage.getMiddleQty();
//                    double lossRate =  box.getLossRate()==null ? 0 : box.getLossRate();
//                    box_qty =  DoubleUtil.upPrecision(demandQty*linkQty*middleQty*consumeQt*1000*(1+lossRate/100), 0);
//                }
//                double spec = tray.getSpecQty();
//                tray_qty = DoubleUtil.upPrecision(spec*box_qty, 0);
//            }
//
//            //分配量、到货量
//            String fab = "CELL";
//            double allocationQty_box = allocationService.getAllocationPackage(fab, product, type, linkQty, mode, box.getMaterial(), fabDate);
//            double arrivalQty_box = arrivalPlanService.getArrivalPlanPackage(fab, product, type, linkQty, mode, box.getMaterial(), fabDate);
//            double allocationQty_tray = allocationService.getAllocationPackage(fab, product, type, linkQty, mode, tray.getMaterial(), fabDate);
//            double arrivalQty_tray = arrivalPlanService.getArrivalPlanPackage(fab, product, type, linkQty, mode, tray.getMaterial(), fabDate);
//
//            MrpPackage mrpPackageBox = new MrpPackage();
//            mrpPackageBox.setFabDate(fabDate);
//            mrpPackageBox.setVer(ver);
//            mrpPackageBox.setProduct(product);
//            mrpPackageBox.setType(type);
//            mrpPackageBox.setLinkQty(linkQty);
//            mrpPackageBox.setMode(mode);
//            mrpPackageBox.setMaterial(box.getMaterial());
//            mrpPackageBox.setMaterialName(box.getMaterialName());
//            mrpPackageBox.setMaterialGroup(box.getMaterialGroup());
//            mrpPackageBox.setMaterialGroupName(box.getMaterialGroupName());
//            mrpPackageBox.setDemandQty(box_qty);
//            mrpPackageBox.setArrivalQty(arrivalQty_box);
//            mrpPackageBox.setAllocationQty(allocationQty_box);
//
//            MrpPackage mrpPackageTray = new MrpPackage();
//            mrpPackageTray.setFabDate(fabDate);
//            mrpPackageTray.setVer(ver);
//            mrpPackageTray.setProduct(product);
//            mrpPackageTray.setType(type);
//            mrpPackageTray.setLinkQty(linkQty);
//            mrpPackageTray.setMode(mode);
//            mrpPackageTray.setMaterial(tray.getMaterial());
//            mrpPackageTray.setMaterialName(tray.getMaterialName());
//            mrpPackageTray.setMaterialGroup(tray.getMaterialGroup());
//            mrpPackageTray.setMaterialGroupName(tray.getMaterialGroupName());
//            mrpPackageTray.setDemandQty(tray_qty);
//            mrpPackageTray.setArrivalQty(arrivalQty_tray);
//            mrpPackageTray.setAllocationQty(allocationQty_tray);
//
//            mrpPackageList.add(mrpPackageBox);
//            mrpPackageList.add(mrpPackageTray);
//        }
//        mrpService.saveMrpPackage(mrpPackageList);
//    }
//
//    @Override
//    public void computeDemand(String ver) {
//
//    }
//
//    @Override
//    public void computeDpsDemand(String ver, String dpsVer) {
//
//    }
//
//    @Override
//    public void computeDpsDemand(String ver, String dpsVer, String product, String type, Double linkQty, String mode) {
//
//    }
//
//    @Override
//    public void computeMrpMaterial(String ver) {
//
//    }
//
//    @Override
//    public void computeMrpBalance(String ver) {
//
//    }
//
//    @Override
//    public void computeMrpBalance(String ver, String product, String type, Double linkQty, String mode, String material) {
//
//    }


    @Override
    public void runMrp(String[] dpsVers, String user) {
        MrpVer mrpVer = createMrpVer(dpsVers, user);
        computeMrpPackage(mrpVer.getVer());
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
        mrp.setType(MrpVer.Type_Package);
        mrp.setFab(fab);
        mrp.setMemo("运算中");
        mrp.setCreator(user);
        mrpService.saveMrpVer(mrp);
        return mrp;
    }

    @Override
    public void computeMrpPackage(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
        List<String> dpsVerList = Arrays.asList(dpsVers);
        List<String> packageIdList = dpsPackageService.getPackageId(dpsVerList);

        for(String packageId : packageIdList) {
            List<DpsPackage> dpsPackageList = dpsPackageService.getDpsPackage(dpsVerList, packageId);
            BomPackage bomPackage = bomPackageService.getBomPackageById(packageId);
            List<BomPackageMaterial> bomPackageMaterialList = bomPackageService.getBomPackageForSupplier(packageId);

            List<MrpPackageMaterial> mrpPackageMaterialList = new ArrayList<>();
            List<MrpPackage> mrpPackageList = new ArrayList<>();
            //纸箱
            BomPackageMaterial box = null;
            //Tray
            List<BomPackageMaterial> otherList = new ArrayList<>();
            for(BomPackageMaterial bomPackageMaterial : bomPackageMaterialList) {
                MrpPackageMaterial mrpPackageMaterial = new MrpPackageMaterial();
                mrpPackageMaterial.setVer(ver);
                mrpPackageMaterial.setPackageId(packageId);
                mrpPackageMaterial.setMaterial(bomPackageMaterial.getMaterial());
                mrpPackageMaterial.setMaterialName(bomPackageMaterial.getMaterialName());
                mrpPackageMaterial.setMaterialGroup(bomPackageMaterial.getMaterialGroup());
                mrpPackageMaterial.setMaterialGroupName(bomPackageMaterial.getMaterialGroupName());
                mrpPackageMaterial.setProduct(bomPackage.getProduct());
                mrpPackageMaterial.setType(bomPackage.getType());
                mrpPackageMaterial.setLinkQty(bomPackage.getLinkQty());
                mrpPackageMaterial.setMode(bomPackage.getMode());
                mrpPackageMaterial.setFab("CELL");
                mrpPackageMaterialList.add(mrpPackageMaterial);

                if(box==null && StringUtils.containsIgnoreCase(bomPackageMaterial.getMaterialName(), "箱")) {
                    box = bomPackageMaterial;
                } else {
                    otherList.add(bomPackageMaterial);
                }
            }

            String type = bomPackage.getType();
            for(DpsPackage dpsPackage : dpsPackageList) {
                Date fabDate = dpsPackage.getFabDate();
                //DPS需求量
                double dspDemandQty = dpsPackage.getDemandQty();

                //纸箱需求量
                double boxDemandQty = 0;
                if(box != null) {
                    try{
                        if(type.equals(BomPackage.TYPE_D)) {
                            //单片
                            String mode = bomPackage.getMode();
                            if(mode.equals("全切单")) {
                                //全切单：cellInput*1000*54切数/120Panel数目*1.1（10%损耗） (全切单)
                                double cutQty = Double.valueOf(bomPackage.getCutQty());
                                double panelQty = Double.valueOf(bomPackage.getPanelQty());
                                double lossRat = Double.valueOf(box.getLossRate());
                                boxDemandQty = DoubleUtil.upPrecision(dspDemandQty*1000*cutQty*panelQty*(1+lossRat/100), 0);
                            } else {
                                //抽单模式： cellinput*1000/600*3（抽单模式）
                                String[] modeS = mode.split("抽");
                                double mode_1 = Double.valueOf(modeS[0]);
                                double mode_2 = Double.valueOf(modeS[1]);
                                boxDemandQty =  DoubleUtil.upPrecision(dspDemandQty*1000/mode_1*mode_2, 0);
                            }
                        } else {
                            //连片：cellinput*连片数40*中板数3*单耗量G*1000*1.05（5%损耗）
                            //单耗量G: 包装规格48倍/连数40/装箱数Panel40
                            double linkQty = Double.valueOf(bomPackage.getLinkQty());
                            double middleQty = Double.valueOf(bomPackage.getMiddleQty());
                            double lossRat = Double.valueOf(box.getLossRate());
                            double panelQty = Double.valueOf(bomPackage.getPanelQty());
                            double specQty = Double.valueOf(box.getSpecQty());
                            boxDemandQty = DoubleUtil.upPrecision(dspDemandQty*1000*linkQty*middleQty*(specQty/linkQty/panelQty)*(1+lossRat/100), 0);
                        }
                    } catch (Exception e) {
                        mrpWarnService.addWarn(ver, packageId, "DPS", "包材BOM有问题，"+e.getMessage());
                    }


                    MrpPackage mrpPackage = new MrpPackage();
                    mrpPackage.setVer(ver);
                    mrpPackage.setFabDate(fabDate);
                    mrpPackage.setPackageId(packageId);
                    mrpPackage.setMaterial(box.getMaterial());
                    mrpPackage.setProduct(bomPackage.getProduct());
                    mrpPackage.setType(bomPackage.getType());
                    mrpPackage.setLinkQty(bomPackage.getLinkQty());
                    mrpPackage.setDemandQty(boxDemandQty);
                    mrpPackageList.add(mrpPackage);
                }

                //Tray
                for(BomPackageMaterial bomPackageMaterial : otherList) {
                    //包装规格*纸箱需求量
                    double specQty = Double.valueOf(bomPackageMaterial.getSpecQty());
                    double demandQty = DoubleUtil.upPrecision(specQty*boxDemandQty, 0);
                    MrpPackage mrpPackage = new MrpPackage();
                    mrpPackage.setVer(ver);
                    mrpPackage.setFabDate(fabDate);
                    mrpPackage.setPackageId(packageId);
                    mrpPackage.setMaterial(bomPackageMaterial.getMaterial());
                    mrpPackage.setProduct(bomPackage.getProduct());
                    mrpPackage.setType(bomPackage.getType());
                    mrpPackage.setLinkQty(bomPackage.getLinkQty());
                    mrpPackage.setDemandQty(demandQty);
                    mrpPackageList.add(mrpPackage);
                }
            }

            mrpPackageService.saveMrpPackageMaterial(mrpPackageMaterialList);
            mrpPackageService.saveMrpPackage(mrpPackageList);
        }
    }
}
