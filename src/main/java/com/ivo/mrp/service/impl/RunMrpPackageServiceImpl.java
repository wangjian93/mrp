package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.BomPackageMaterial;
import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class RunMrpPackageServiceImpl implements RunMrpPackageService {

    private RunMrpService runMrpService;
    private MrpService mrpService;
    private BomPackageService bomPackageService;
    private DpsService dpsService;
    private AllocationService allocationService;
    private ArrivalPlanService arrivalPlanService;

    public RunMrpPackageServiceImpl(RunMrpService runMrpService, MrpService mrpService, BomPackageService bomPackageService,
                                    DpsService dpsService, AllocationService allocationService,
                                    ArrivalPlanService arrivalPlanService) {
        this.runMrpService = runMrpService;
        this.mrpService = mrpService;
        this.bomPackageService = bomPackageService;
        this.dpsService = dpsService;
        this.allocationService = allocationService;
        this.arrivalPlanService = arrivalPlanService;
    }

    @Override
    public void runMrp(String[] dpsVers, String[] mpsVers, String user) {
        MrpVer mrpVer = runMrpService.createMrpVer(dpsVers, mpsVers, user);
        String type = mrpVer.getType();
        String ver = mrpVer.getVer();
        if(type.equals(MrpVer.Type_Package)) {
            computeMrpPackage(ver);
        }
    }

    @Override
    public void computeMrpPackage(String ver) {
        log.info("计算MRP版本" + ver +"的需求>> START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String[] dpsVers = mrpService.convertStringToAry(mrpVer.getDpsVer());
        for(String dpsVer : dpsVers) {
            computeMrpPackage(ver, dpsVer);
        }
        log.info("计算MRP版本" + ver +"的需求>> END");
    }

    @Override
    public void computeMrpPackage(String ver, String dpsVer) {
        log.info("计算MRP版本"+ver+"的需求量>> DPS" + dpsVer);
        List<Map> mapList = dpsService.getDpsPackageProduct(dpsVer);
        int l = mapList.size();
        for(Map map : mapList) {
            String product = (String) map.get("product");
            String type = (String) map.get("type");
            Double linkQty = (Double) map.get("linkQty");
            String mode = (String) map.get("mode");
            log.info("计算机种"+product+" "+type+",剩余"+l--);
            computeMrpPackage(ver, dpsVer, product, type, linkQty, mode);
        }
    }

    @Override
    public void computeMrpPackage(String ver, String dpsVer, String product, String type, Double linkQty, String mode) {
        //BOM纸箱、tray盘
        BomPackage bomPackage = bomPackageService.getBomPackage(product, type, linkQty, mode);
        if(bomPackage == null) {
            log.warn("警告：包材机种"+product+type+"没有BOM List，MRP版本"+ver);
            return;
        }
        BomPackageMaterial box = null;
        BomPackageMaterial tray = null;
        for(BomPackageMaterial bomPackageMaterial : bomPackage.getMaterialList()) {
            if(bomPackageMaterial.isSupplierFlag()) {
                if(StringUtils.containsIgnoreCase(bomPackageMaterial.getMaterialName(), "Tray")) {
                    tray = bomPackageMaterial;
                } else if(StringUtils.contains(bomPackageMaterial.getMaterialName(), "箱")) {
                    box = bomPackageMaterial;
                } else {
                    tray = bomPackageMaterial;
                }
            }
        }
        if(box == null || tray == null) {
            log.warn("警告：包材机种"+product+type+"的BOM List中没有纸箱或Tray盘，MRP版本"+ver);
            return;
        }

        //需求量
        List<DpsPackage> dpsPackageList = dpsService.getDpsPackage(dpsVer, product, type, linkQty, mode);

        mrpService.deleteMrpPackage(mrpService.getMrpPackage(ver, product, type, linkQty, mode));
        List<MrpPackage> mrpPackageList = new ArrayList<>();
        for(DpsPackage dpsPackage : dpsPackageList) {
            java.sql.Date fabDate = dpsPackage.getFabDate();
            double demandQty = dpsPackage.getDemandQty();
            double box_qty = 0;
            double tray_qty = 0;

            if(demandQty>0) {
                // 单片
                if(StringUtils.equals(type, BomPackage.TYPE_D )) {
                    if(StringUtils.equals(mode, "全切单")) {
                        //cellInput*1000*54切数/120Panel数目*1.1（10%损耗） (全切单)
                        double lossRate =  box.getLossRate()==null ? 0 : box.getLossRate();
                        double cutQty = bomPackage.getCutQty();
                        double panelQty = bomPackage.getPanelQty();
                        box_qty = DoubleUtil.upPrecision(demandQty*1000*cutQty/panelQty*(1+lossRate/100), 0);
                    } else {
                        //cellinput*1000/600*3（抽单模式）
                        String[] modeS = mode.split("抽");
                        double mode_1 = Double.valueOf(modeS[0]);
                        double mode_2 = Double.valueOf(modeS[1]);
                        box_qty =  DoubleUtil.upPrecision(demandQty*1000/mode_1*mode_2, 0);
                    }
                } else {
                    //连片
                    //cellinput*连片数40*中板数3*单耗量G*1000*1.05（5%损耗）
                    double consumeQt = box.getConsumeQty();
                    double middleQty = bomPackage.getMiddleQty();
                    double lossRate =  box.getLossRate()==null ? 0 : box.getLossRate();
                    box_qty =  DoubleUtil.upPrecision(demandQty*linkQty*middleQty*consumeQt*1000*(1+lossRate/100), 0);
                }
                double spec = tray.getSpecQty();
                tray_qty = DoubleUtil.upPrecision(spec*box_qty, 0);
            }

            //分配量、到货量
            String fab = "CELL";
            double allocationQty_box = allocationService.getAllocationPackage(fab, product, type, linkQty, mode, box.getMaterial(), fabDate);
            double arrivalQty_box = arrivalPlanService.getArrivalPlanPackage(fab, product, type, linkQty, mode, box.getMaterial(), fabDate);
            double allocationQty_tray = allocationService.getAllocationPackage(fab, product, type, linkQty, mode, tray.getMaterial(), fabDate);
            double arrivalQty_tray = arrivalPlanService.getArrivalPlanPackage(fab, product, type, linkQty, mode, tray.getMaterial(), fabDate);

            MrpPackage mrpPackageBox = new MrpPackage();
            mrpPackageBox.setFabDate(fabDate);
            mrpPackageBox.setVer(ver);
            mrpPackageBox.setProduct(product);
            mrpPackageBox.setType(type);
            mrpPackageBox.setLinkQty(linkQty);
            mrpPackageBox.setMode(mode);
            mrpPackageBox.setMaterial(box.getMaterial());
            mrpPackageBox.setMaterialName(box.getMaterialName());
            mrpPackageBox.setMaterialGroup(box.getMaterialGroup());
            mrpPackageBox.setMaterialGroupName(box.getMaterialGroupName());
            mrpPackageBox.setDemandQty(box_qty);
            mrpPackageBox.setArrivalQty(arrivalQty_box);
            mrpPackageBox.setAllocationQty(allocationQty_box);

            MrpPackage mrpPackageTray = new MrpPackage();
            mrpPackageTray.setFabDate(fabDate);
            mrpPackageTray.setVer(ver);
            mrpPackageTray.setProduct(product);
            mrpPackageTray.setType(type);
            mrpPackageTray.setLinkQty(linkQty);
            mrpPackageTray.setMode(mode);
            mrpPackageTray.setMaterial(tray.getMaterial());
            mrpPackageTray.setMaterialName(tray.getMaterialName());
            mrpPackageTray.setMaterialGroup(tray.getMaterialGroup());
            mrpPackageTray.setMaterialGroupName(tray.getMaterialGroupName());
            mrpPackageTray.setDemandQty(tray_qty);
            mrpPackageTray.setArrivalQty(arrivalQty_tray);
            mrpPackageTray.setAllocationQty(allocationQty_tray);

            mrpPackageList.add(mrpPackageBox);
            mrpPackageList.add(mrpPackageTray);
        }
        mrpService.saveMrpPackage(mrpPackageList);
    }

    @Override
    public void computeDemand(String ver) {

    }

    @Override
    public void computeDpsDemand(String ver, String dpsVer) {

    }

    @Override
    public void computeDpsDemand(String ver, String dpsVer, String product, String type, Double linkQty, String mode) {

    }

    @Override
    public void computeMrpMaterial(String ver) {

    }

    @Override
    public void computeMrpBalance(String ver) {

    }

    @Override
    public void computeMrpBalance(String ver, String product, String type, Double linkQty, String mode, String material) {

    }
}
