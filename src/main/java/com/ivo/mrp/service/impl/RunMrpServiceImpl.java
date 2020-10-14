package com.ivo.mrp.service.impl;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.StringUtil;
import com.ivo.mrp.entity.*;
import com.ivo.mrp.entity.direct.ary.*;
import com.ivo.mrp.entity.direct.cell.*;
import com.ivo.mrp.entity.direct.lcm.*;
import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.BomPackageMaterial;
import com.ivo.mrp.entity.packaging.DpsPackage;
import com.ivo.mrp.entity.packaging.MrpPackage;
import com.ivo.mrp.exception.MrpException;
import com.ivo.mrp.service.*;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class RunMrpServiceImpl implements RunMrpService {

    private DpsService dpsService;
    private MpsService mpsService;
    private MrpService mrpService;
    private MonthSettleService monthSettleService;
    private BomService bomService;
    private DemandService demandService;
    private CutService cutService;
    private LossRateService lossRateService;
    private RestService restService;
    private ArrivalPlanService arrivalPlanService;
    private AllocationService allocationService;
    private BomPackageService bomPackageService;
    private MaterialService materialService;
    private MaterialGroupService materialGroupService;
    private SupplierService supplierService;

    @Autowired
    public RunMrpServiceImpl(DpsService dpsService, MpsService mpsService, MrpService mrpService,
                             MonthSettleService monthSettleService, BomService bomService,
                             DemandService demandService,
                             CutService cutService,
                             LossRateService lossRateService,
                             RestService restService,
                             ArrivalPlanService arrivalPlanService,
                             AllocationService allocationService,
                             BomPackageService bomPackageService,
                              MaterialService materialService,
                             MaterialGroupService materialGroupService,
                             SupplierService supplierService) {
        this.dpsService = dpsService;
        this.mpsService = mpsService;
        this.mrpService = mrpService;
        this.monthSettleService = monthSettleService;
        this.bomService = bomService;
        this.demandService = demandService;
        this.cutService = cutService;
        this.lossRateService = lossRateService;
        this.restService = restService;
        this.arrivalPlanService = arrivalPlanService;
        this.allocationService = allocationService;
        this.bomPackageService = bomPackageService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.supplierService = supplierService;
    }

    @Override
    public MrpVer createMrpVer(String[] dpsVers, String[] mpsVers, String user) {
        MrpVer mrpVer = checkDpsAndMps(dpsVers, mpsVers);
        String ver = generateMpsVer();
        log.info("生成MRP版本" + ver);
        mrpVer.setVer(ver);
        mrpVer.setCreator(user);
        mrpService.saveMrpVer(mrpVer);
        return mrpVer;
    }

    @Override
    public MrpVer checkDpsAndMps(String[] dpsVers, String[] mpsVers) {
        //检查DPS、MPS的版本、厂别、日期区间
        log.info("检查DPS "+ Arrays.toString(dpsVers) +" & MPS " + Arrays.toString(mpsVers));
        if(dpsVers == null || mpsVers==null || (dpsVers.length+mpsVers.length)==0)
            throw new MrpException("选择的DPS&MPS版本不能为空");
        String fab = null;
        String type = null;
        List<java.sql.Date[]> dateList = new ArrayList<>();
        Set<String> dpsVerSet = new HashSet<>();
        for(String ver : dpsVers) {
            if(dpsVerSet.contains(ver)) {
                throw new MrpException("选择的DPS版本" + ver + "重复");
            } else {
                dpsVerSet.add(ver);
            }
            DpsVer dpsVer = dpsService.getDpsVer(ver);
            if(dpsVer == null)
                throw new MrpException("系统中DPS版本" + ver + "不存在");
            if(dpsVer.getFab()==null || dpsVer.getFab().equals("") || dpsVer.getStartDate() == null
                    || dpsVer.getEndDate() == null || dpsVer.getEndDate().before(dpsVer.getStartDate()))
                throw new MrpException("系统中DPS版本" + ver + "信息不准确");
            if(fab == null) {
                fab = dpsVer.getFab().toUpperCase();
            } else if(!StringUtils.equalsIgnoreCase(fab, dpsVer.getFab())) {
                throw new MrpException("所选DPS版本" + Arrays.toString(dpsVers) + "的厂别不一致");
            }
            if(type == null) {
                type = dpsVer.getType().toUpperCase();
            } else if(!StringUtils.equalsIgnoreCase(type, dpsVer.getType())) {
                throw new MrpException("所选DPS版本" + Arrays.toString(dpsVers) + "的类型不一致");
            }

            java.sql.Date[] dates = new java.sql.Date[] {dpsVer.getStartDate(), dpsVer.getEndDate()};
            dateList.add(dates);
        }
        Set<String> mpsVerSet = new HashSet<>();
        for(String ver : mpsVers) {
            if(mpsVerSet.contains(ver)) {
                throw new MrpException("选择的MPS版本" + ver + "重复");
            } else {
                mpsVerSet.add(ver);
            }
            MpsVer mpsVer = mpsService.getMpsVer(ver);
            if(mpsVer == null) throw new MrpException("系统中MPS版本" + ver + "不存在");
            if(mpsVer.getFab()==null || mpsVer.getFab().equals("") || mpsVer.getStartDate() == null
                    || mpsVer.getEndDate() == null || mpsVer.getEndDate().before(mpsVer.getStartDate()))
                throw new MrpException("系统中MPS版本" + ver + "信息不准确");
            if(fab == null) {
                fab = mpsVer.getFab().toUpperCase();
            } else if(!StringUtils.equalsIgnoreCase(fab, mpsVer.getFab())) {
                throw new MrpException("所选MPS版本" + Arrays.toString(dpsVers) + "的厂别不一致");
            }
            if(type == null) {
                type = mpsVer.getType().toUpperCase();
            } else if(!StringUtils.equalsIgnoreCase(type, mpsVer.getType())) {
                throw new MrpException("所选MPS版本" + Arrays.toString(dpsVers) + "的类型不一致");
            }
            java.sql.Date[] dates = new java.sql.Date[] {mpsVer.getStartDate(), mpsVer.getEndDate()};
            dateList.add(dates);
        }

        if(!StringUtils.equalsAny(fab, "LCM1", "LCM2", "CELL", "ARY"))
            throw new MrpException("DPS&MPS版本的厂别"+fab+"不正确");
        if(!StringUtils.equalsAny(type, "LCM", "ARY", "CELL", "包材"))
            throw new MrpException("DPS&MPS版本的类型"+type+"不正确");

        java.sql.Date[] dates = checkRangeDate(dateList.toArray(new java.sql.Date[dateList.size()][2]));
        MrpVer mrpVer = new MrpVer();
        assert fab != null;
        mrpVer.setFab(fab);
        mrpVer.setStartDate(new java.sql.Date(DateUtil.getFirstDayOfMonth(dates[0]).getTime()));
        mrpVer.setEndDate(dates[1]);
        mrpVer.setDpsVer(mrpService.convertAryToString(dpsVers));
        mrpVer.setMpsVer(mrpService.convertAryToString(mpsVers));
        mrpVer.setType(type);
        return mrpVer;
    }

    /**
     * 判断日期区间是否有重合
     * @param intervals 日期区间数组
     * @return Date[] 日期区间
     */
    private static java.sql.Date[] checkRangeDate(java.sql.Date[][] intervals) {
        //边界判断
        if (intervals.length <= 1) {
            return intervals[0];
        }
        //先按起点位置进行排序
        Arrays.sort(intervals, Comparator.comparingLong(o -> o[0].getTime()));
        //利用list存储合并好的区间
        List<java.sql.Date[]> result = new ArrayList<>();
        //初始时将第一个区间放入list中
        result.add(intervals[0]);
        //记录上一合并好的区间在list中的位置
        int last = 0;
        //遍历并合并后面各区间
        for (int i = 1; i < intervals.length; i++) {
            //上一区间的终点
            java.sql.Date lastEnd = result.get(last)[1];
            //当前区间的起点
            java.sql.Date start = intervals[i][0];
            if(start.before(lastEnd)) {
                throw new MrpException("时间区间重合");
            } else {
                result.add(intervals[i]);
            }
        }
        return new java.sql.Date[] {result.get(0)[0], result.get(result.size()-1)[1]};
    }

    @Override
    public String generateMpsVer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = String.format("%03d", (mrpService.countMrp()+1)%1000);
        return  sdf.format(new java.util.Date())+str;
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
        // 上月结余
        computeSettleDemand(ver);
        log.info(msg + "END");
    }

    @Override
    public void computeDpsDemand(String ver, String dpsVer) {
        String msg = "计算MRP版本"+ver+"的需求量>> DPS" + dpsVer ;
        log.info(msg);
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String type = dps.getType();
        switch (type) {
            case DpsVer.Type_Lcm:
                computeDpsDemandLcm(ver, dpsVer);
                break;
            case DpsVer.Type_Cell:
                computeDpsDemandCell(ver, dpsVer);
                break;
            case DpsVer.Type_Ary:
                computeDpsDemandAry(ver, dpsVer);
                break;
        }
    }

    @Override
    public void computeDpsDemandLcm(String ver, String dpsVer) {
        List<String> productList = dpsService.getDpsLcmProduct(dpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeDpsDemandLcm(ver, dpsVer, product);
        }
    }

    @Override
    public void computeDpsDemandAry(String ver, String dpsVer) {
        List<String> productList = dpsService.getDpsAryProduct(dpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeDpsDemandAry(ver, dpsVer, product);
        }
        //Ary2次input OC材料
        List<String> productOcList = dpsService.getDpsAryOcProduct(dpsVer);
        int l2 = productOcList.size();
        for(String product : productOcList) {
            log.info("机种" + product + "，剩余" + l2--);
            computeDpsDemandAryOc(ver, dpsVer, product);
        }
    }

    @Override
    public void computeDpsDemandCell(String ver, String dpsVer) {
        List<String> productList = dpsService.getDpsCellProduct(dpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeDpsDemandCell(ver, dpsVer, product);
        }
    }

    @Override
    public void computeDpsDemand(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String type = dps.getType();
        switch (type) {
            case DpsVer.Type_Lcm:
                computeDpsDemandLcm(ver, dpsVer, product);
                break;
            case DpsVer.Type_Cell:
                computeDpsDemandCell(ver, dpsVer, product);
                break;
            case DpsVer.Type_Ary:
                computeDpsDemandAry(ver, dpsVer, product);
                computeDpsDemandAryOc(ver, dpsVer, product);
                break;
        }
    }

    @Override
    public void computeDpsDemandLcm(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String fab = dps.getFab();
        List<BomLcm> bomLcmList = bomService.getLcmBom(product, fab);
        if(bomLcmList == null || bomLcmList.size()==0) {
            log.warn("警告：DPS机种"+product+"没有BOM List，MRP版本"+ver);
            return;
        }
        List<DpsLcm> dpsLcmList = dpsService.getDpsLcm(dpsVer, product);
        List<DemandLcm> demandLcmList = new ArrayList<>();
        for(DpsLcm dpsLcm : dpsLcmList) {
            for(BomLcm bomLcm : bomLcmList) {
                DemandLcm demandLcm = new DemandLcm();
                demandLcm.setVer(ver);
                demandLcm.setType(DemandLcm.TYPE_DPS);
                demandLcm.setDpsMpsVer(dpsVer);
                demandLcm.setFab(fab);
                demandLcm.setProduct(dpsLcm.getProduct());
                demandLcm.setProject(dpsLcm.getProject());
                demandLcm.setFabDate(dpsLcm.getFabDate());
                demandLcm.setQty(dpsLcm.getDemandQty());
                demandLcm.setMaterial(bomLcm.getMaterial());
                demandLcm.setUsageQty(bomLcm.getUsageQty());
                //替代料比例
                Double substituteRate = bomLcm.getSubstituteRate();//substituteService.getSubstituteRate(bomLcm.getFab(), bomLcm.getProduct(), bomLcm.getMaterialGroup(), bomLcm.getMaterial());
                demandLcm.setSubstituteRate(substituteRate);

                //DPS需求量 * Bon使用量 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandLcm.getQty()*demandLcm.getUsageQty()*(substituteRate/100), 0);
                demandLcm.setDemandQty(demandQty);
                demandLcmList.add(demandLcm);
            }
        }
        demandService.saveDemandLcm(demandLcmList);
    }

    @Override
    public void computeDpsDemandAry(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String fab = dps.getFab();
        List<BomAry> bomAryList = bomService.getAryBom(product);
        if(bomAryList == null || bomAryList.size()==0) {
            log.warn("警告：DPS机种"+product+"没有BOM List，MRP版本"+ver);
            return;
        }
        Double cut = cutService.getProjectCut(product);
        if(cut == null) {
            log.warn("警告：DPS机种"+product+"没有切片数，MRP版本"+ver);
            return;
        }
        List<DpsAry> dpsAryList = dpsService.getDpsAry(dpsVer, product);
        List<DemandAry> demandAryList = new ArrayList<>();
        for(DpsAry dpsAry : dpsAryList) {
            for(BomAry bomAry : bomAryList) {
                DemandAry demandAry = new DemandAry();
                demandAry.setVer(ver);
                demandAry.setType(DemandLcm.TYPE_DPS);
                demandAry.setDpsMpsVer(dpsVer);
                demandAry.setFab(fab);
                demandAry.setProduct(dpsAry.getProduct());
                demandAry.setFabDate(dpsAry.getFabDate());
                demandAry.setQty(dpsAry.getDemandQty());
                demandAry.setMaterial(bomAry.getMaterial());
                demandAry.setUsageQty(bomAry.getUsageQty());
                demandAry.setCutQty(cut);

                //DPS需求量 * Bon使用量 * 1000 * 切片数
                //保留整数，向上取整
                double demandQty = DoubleUtil.upPrecision(demandAry.getQty()*demandAry.getUsageQty()*1000*cut, 0);
                demandAry.setDemandQty(demandQty);
                demandAryList.add(demandAry);
            }
        }
        demandService.saveDemandAry(demandAryList);
    }

    @Override
    public void computeDpsDemandAryOc(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String fab = dps.getFab();
        List<Map> bomOcList = bomService.getAryOcBom(product);
        if(bomOcList == null || bomOcList.size()==0) {
            log.warn("警告：DPS机种"+product+" ARY 2次Input没有OC BOM List，MRP版本"+ver);
            return;
        }
        Double cut = cutService.getProjectCut(product);
        if(cut == null) {
            log.warn("警告：DPS机种"+product+"没有切片数，MRP版本"+ver);
            return;
        }
        List<DpsAryOc> dpsAryOcList = dpsService.getDpsAryOc(dpsVer, product);
        List<DemandAryOc> demandAryOcList = new ArrayList<>();
        for(DpsAryOc dpsAryOc : dpsAryOcList) {
            for(Map bomOc : bomOcList) {
                DemandAryOc demandAryOc = new DemandAryOc();
                demandAryOc.setVer(ver);
                demandAryOc.setDpsVer(dpsVer);
                demandAryOc.setFab(fab);
                demandAryOc.setProduct(dpsAryOc.getProduct());
                demandAryOc.setFabDate(dpsAryOc.getFabDate());
                demandAryOc.setQty(dpsAryOc.getDemandQty());
                String material = (String) bomOc.get("material");
                double usageQty = (double) bomOc.get("usageQty");
                demandAryOc.setMaterial(material);
                demandAryOc.setUsageQty(usageQty);
                demandAryOc.setCutQty(cut);

                //DPS需求量 * Bon使用量 * 1000 * 切片数
                //保留整数，向上取整
                double demandQty = DoubleUtil.upPrecision(demandAryOc.getQty()*demandAryOc.getUsageQty()*1000*cut, 0);
                demandAryOc.setDemandQty(demandQty);
                demandAryOcList.add(demandAryOc);
            }
        }
        demandService.saveDemandAryOc(demandAryOcList);
    }

    @Override
    public void computeDpsDemandCell(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String fab = dps.getFab();
        List<BomCellMtrl> bomCellList = bomService.getCellBom(product);
        if(bomCellList == null || bomCellList.size()==0) {
            log.warn("警告：DPS机种"+product+"没有BOM List，MRP版本"+ver);
            return;
        }
        String project = StringUtils.contains(product, "") ? product.substring(0, product.indexOf(" ")) : product;
        Double cut = cutService.getProjectCut(project);
        if(cut == null) {
            log.warn("警告：DPS机种"+product+"没有切片数，MRP版本"+ver);
            return;
        }
        List<DpsCell> dpsCellList = dpsService.getDpsCell(dpsVer, product);
        List<DemandCell> demandCellList = new ArrayList<>();
        for(DpsCell dpsCell : dpsCellList) {
            for(BomCellMtrl bomCellMtrl : bomCellList) {
                DemandCell demandCell = new DemandCell();
                demandCell.setVer(ver);
                demandCell.setType(DemandLcm.TYPE_DPS);
                demandCell.setDpsMpsVer(dpsVer);
                demandCell.setFab(fab);
                demandCell.setProduct(dpsCell.getProduct());
                demandCell.setFabDate(dpsCell.getFabDate());
                demandCell.setQty(dpsCell.getDemandQty());
                demandCell.setMaterial(bomCellMtrl.getMaterial());
                demandCell.setUsageQty(bomCellMtrl.getUsageQty());
                demandCell.setCutQty(cut);
                demandCell.setProject(project);

                //替代料比例
                Double substituteRate = bomCellMtrl.getSubstituteRate();
                demandCell.setSubstituteRate(substituteRate);

                //DPS需求量 * Bon使用量 * 1000 * 切片数 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandCell.getQty()*demandCell.getUsageQty()*1000*cut*(substituteRate/100), 0);
                demandCell.setDemandQty(demandQty);
                demandCellList.add(demandCell);
            }
        }
        demandService.saveDemandCell(demandCellList);
    }

    @Override
    public void computeMpsDemand(String ver, String mpsVer) {
        String msg = "计算MRP版本"+ver+"的需求量>> MPS" + mpsVer ;
        log.info(msg);
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String type = mps.getType();
        switch (type) {
            case DpsVer.Type_Lcm:
                computeMpsDemandLcm(ver, mpsVer);
                break;
            case DpsVer.Type_Cell:
                computeMpsDemandCell(ver, mpsVer);
                break;
            case DpsVer.Type_Ary:
                computeMpsDemandAry(ver, mpsVer);
                break;
        }
    }

    @Override
    public void computeMpsDemandLcm(String ver, String mpsVer) {
        List<String> productList = mpsService.getMpsLcmProduct(mpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeMpsDemandLcm(ver, mpsVer, product);
        }
    }

    @Override
    public void computeMpsDemandAry(String ver, String mpsVer) {
        List<String> productList = mpsService.getMpsAryProduct(mpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeMpsDemandAry(ver, mpsVer, product);
        }
    }

    @Override
    public void computeMpsDemandCell(String ver, String mpsVer) {
        List<String> productList = mpsService.getMpsCellProduct(mpsVer);
        int l = productList.size();
        for(String product : productList) {
            log.info("机种" + product + "，剩余" + l--);
            computeMpsDemandCell(ver, mpsVer, product);
        }
    }

    @Override
    public void computeMpsDemand(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String type = mps.getType();
        switch (type) {
            case DpsVer.Type_Lcm:
                computeMpsDemandLcm(ver, mpsVer, product);
                break;
            case DpsVer.Type_Cell:
                computeMpsDemandCell(ver, mpsVer, product);
                break;
            case DpsVer.Type_Ary:
                computeMpsDemandAry(ver, mpsVer, product);
                break;
        }
    }

    @Override
    public void computeMpsDemandLcm(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String fab = mps.getFab();
        List<BomLcm> bomLcmList = bomService.getLcmBom(product, fab);
        if(bomLcmList == null || bomLcmList.size()==0) {
            log.warn("警告：MPS机种"+product+"没有BOM List，MRP版本"+ver);
            return;
        }

        List<MpsLcm> mpsLcmList = mpsService.getMpsLcm(mpsVer, product);
        List<DemandLcm> demandLcmList = new ArrayList<>();
        for(MpsLcm mpsLcm : mpsLcmList) {
            for(BomLcm bomLcm : bomLcmList) {
                DemandLcm demandLcm = new DemandLcm();
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
                Double substituteRate = bomLcm.getSubstituteRate();//substituteService.getSubstituteRate(bomLcm.getFab(), bomLcm.getProduct(), bomLcm.getMaterialGroup(), bomLcm.getMaterial());
                demandLcm.setSubstituteRate(substituteRate);

                //MPS需求量 * Bon使用量 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandLcm.getQty()*demandLcm.getUsageQty()*(substituteRate/100), 0);
                demandLcm.setDemandQty(demandQty);
                demandLcmList.add(demandLcm);
            }
        }
        demandService.saveDemandLcm(demandLcmList);
    }

    @Override
    public void computeMpsDemandAry(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String fab = mps.getFab();
        List<BomAry> bomAryList = bomService.getAryBom(product);
        if(bomAryList == null || bomAryList.size()==0) {
            log.warn("警告：MPS机种"+product+"没有BOM List，MRP版本"+ver);
            return;
        }
        Double cut = cutService.getProjectCut(product);
        if(cut == null) {
            log.warn("警告：MPS机种"+product+"没有切片数，MRP版本"+ver);
            return;
        }
        List<MpsAry> mpsAryList = mpsService.getMpsAry(mpsVer, product);
        List<DemandAry> demandAryList = new ArrayList<>();
        for(MpsAry mpsAry : mpsAryList) {
            for(BomAry bomAry : bomAryList) {
                DemandAry demandAry = new DemandAry();
                demandAry.setVer(ver);
                demandAry.setType(DemandAry.TYPE_MPS);
                demandAry.setDpsMpsVer(mpsVer);
                demandAry.setFab(fab);
                demandAry.setProduct(mpsAry.getProduct());
                demandAry.setFabDate(mpsAry.getFabDate());
                demandAry.setQty(mpsAry.getDemandQty());
                demandAry.setMaterial(bomAry.getMaterial());
                demandAry.setUsageQty(bomAry.getUsageQty());
                demandAry.setCutQty(cut);

                //MPS需求量 * Bon使用量 * 1000 * 切片数
                //保留整数，向上取整
                double demandQty = DoubleUtil.upPrecision(demandAry.getQty()*demandAry.getUsageQty()*1000*cut, 0);
                demandAry.setDemandQty(demandQty);
                demandAryList.add(demandAry);
            }
        }
        demandService.saveDemandAry(demandAryList);
    }

    @Override
    public void computeMpsDemandCell(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String fab = mps.getFab();
        List<BomCellMtrl> bomCellList = bomService.getCellBom(product);
        if(bomCellList == null || bomCellList.size()==0) {
            log.warn("警告：MPS机种"+product+"没有BOM List，MRP版本"+ver);
            return;
        }
        String project = StringUtils.contains(product, "") ? product.substring(0, product.indexOf(" ")) : product;
        Double cut = cutService.getProjectCut(project);
        if(cut == null) {
            log.warn("警告：MPS机种"+product+"没有切片数，MRP版本"+ver);
            return;
        }
        List<MpsCell> mpsCellList = mpsService.getMpsCell(mpsVer, product);
        List<DemandCell> demandCellList = new ArrayList<>();
        for(MpsCell mpsCell : mpsCellList) {
            for(BomCellMtrl bomCellMtrl : bomCellList) {
                DemandCell demandCell = new DemandCell();
                demandCell.setVer(ver);
                demandCell.setType(DemandLcm.TYPE_MPS);
                demandCell.setDpsMpsVer(mpsVer);
                demandCell.setFab(fab);
                demandCell.setProduct(mpsCell.getProduct());
                demandCell.setFabDate(mpsCell.getFabDate());
                demandCell.setQty(mpsCell.getDemandQty());
                demandCell.setMaterial(bomCellMtrl.getMaterial());
                demandCell.setUsageQty(bomCellMtrl.getUsageQty());
                demandCell.setCutQty(cut);
                demandCell.setProject(project);

                //替代料比例
                Double substituteRate = bomCellMtrl.getSubstituteRate();
                demandCell.setSubstituteRate(substituteRate);

                //DPS需求量 * Bon使用量 * 1000 * 切片数 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandCell.getQty()*demandCell.getUsageQty()*1000*cut*(substituteRate/100), 0);
                demandCell.setDemandQty(demandQty);
                demandCellList.add(demandCell);
            }
        }
        demandService.saveDemandCell(demandCellList);
    }


    @Override
    public void computeSettleDemand(String ver) {
        log.info("计算MRP版本"+ver+"的需求量>> 月结");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        java.sql.Date startDate = mrpVer.getStartDate();
        String month = DateUtil.getLastMonth(startDate);
        String type = mrpVer.getType();
        switch (type) {
            case DpsVer.Type_Lcm:
                computeSettleDemandLcm(ver, fab, month);
                break;
            case DpsVer.Type_Cell:
                computeSettleDemandCell(ver, fab, month);
                break;
            case DpsVer.Type_Ary:
                computeSettleDemandAry(ver, fab, month);
                break;
        }
    }

    @Override
    public void computeSettleDemandLcm(String ver, String fab, String month) {
        List<MonthSettle> monthSettleList = monthSettleService.getMonthSettle(fab, month);
        int l = monthSettleList.size();
        for(MonthSettle monthSettle : monthSettleList) {
            String product = monthSettle.getProduct();
            log.info("机种" + product + "，剩余" + l--);
            java.sql.Date fabDate = monthSettle.getSettleDate();
            double settleQty = monthSettle.getSettleQty();
            String materialGroup = monthSettle.getMaterialGroup();
            List<BomLcm> bomLcmList = bomService.getLcmBom(product, fab);
            if(bomLcmList == null || bomLcmList.size()==0) {
                log.warn("警告：月结机种"+product+"没有BOM List，MRP版本"+ver);
                return;
            }
            List<DemandLcm> demandLcmList = new ArrayList<>();
            for(BomLcm bomLcm : bomLcmList) {
                //要属于月结机种的同个物料组继续
                if(!bomLcm.getMaterialGroup().equals(materialGroup)) continue;
                DemandLcm demandLcm = new DemandLcm();
                demandLcm.setVer(ver);
                demandLcm.setType(DemandLcm.TYPE_Settle);
                demandLcm.setFab(fab);
                demandLcm.setProduct(product);
                demandLcm.setFabDate(fabDate);
                demandLcm.setQty(settleQty);
                demandLcm.setMaterial(bomLcm.getMaterial());
                demandLcm.setUsageQty(bomLcm.getUsageQty());
                //替代料比例
                Double substituteRate = bomLcm.getSubstituteRate();//substituteService.getSubstituteRate(bomLcm.getFab(), bomLcm.getProduct(), bomLcm.getMaterialGroup(), bomLcm.getMaterial());
                demandLcm.setSubstituteRate(substituteRate);

                //MPS需求量 * Bon使用量 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandLcm.getQty()*demandLcm.getUsageQty()*(substituteRate/100), 0);
                demandLcm.setDemandQty(demandQty);
                demandLcmList.add(demandLcm);
            }
            demandService.saveDemandLcm(demandLcmList);
        }
    }

    @Override
    public void computeSettleDemandAry(String ver, String fab, String month) {
        List<MonthSettle> monthSettleList = monthSettleService.getMonthSettle(fab, month);
        int l = monthSettleList.size();
        for(MonthSettle monthSettle : monthSettleList) {
            String product = monthSettle.getProduct();
            log.info("机种" + product + "，剩余" + l--);
            java.sql.Date fabDate = monthSettle.getSettleDate();
            double settleQty = monthSettle.getSettleQty();
            String materialGroup = monthSettle.getMaterialGroup();
            List<BomAry> bomAryList = bomService.getAryBom(product);
            if(bomAryList == null || bomAryList.size()==0) {
                log.warn("警告：月结机种"+product+"没有BOM List，MRP版本"+ver);
                return;
            }
            Double cut = cutService.getProjectCut(product);
            if(cut == null) {
                log.warn("警告：月结机种"+product+"没有切片数，MRP版本"+ver);
                return;
            }
            List<DemandAry> demandAryList = new ArrayList<>();
            for(BomAry bomAry : bomAryList) {
                //要属于月结机种的同个物料组继续
                if(!bomAry.getMaterialGroup().equals(materialGroup)) continue;
                DemandAry demandAry = new DemandAry();
                demandAry.setVer(ver);
                demandAry.setType(DemandLcm.TYPE_Settle);
                demandAry.setFab(fab);
                demandAry.setProduct(product);
                demandAry.setFabDate(fabDate);
                demandAry.setQty(settleQty);
                demandAry.setMaterial(bomAry.getMaterial());
                demandAry.setUsageQty(bomAry.getUsageQty());

                //MPS需求量 * Bon使用量 * 1000 * 切片数
                //保留整数，向上取整
                double demandQty = DoubleUtil.upPrecision(demandAry.getQty()*demandAry.getUsageQty()*1000*cut, 0);
                demandAry.setDemandQty(demandQty);
                demandAryList.add(demandAry);
            }
            demandService.saveDemandAry(demandAryList);
        }
    }

    @Override
    public void computeSettleDemandCell(String ver, String fab, String month) {
        List<MonthSettle> monthSettleList = monthSettleService.getMonthSettle(fab, month);
        int l = monthSettleList.size();
        for(MonthSettle monthSettle : monthSettleList) {
            String product = monthSettle.getProduct();
            log.info("机种" + product + "，剩余" + l--);
            java.sql.Date fabDate = monthSettle.getSettleDate();
            double settleQty = monthSettle.getSettleQty();
            String materialGroup = monthSettle.getMaterialGroup();
            List<BomCellMtrl> bomCellList = bomService.getCellBom(product);
            if(bomCellList == null || bomCellList.size()==0) {
                log.warn("警告：月结机种"+product+"没有BOM List，MRP版本"+ver);
                return;
            }
            String project = StringUtils.contains(product, "") ? product.substring(0, product.indexOf(" ")) : product;
            Double cut = cutService.getProjectCut(project);
            if(cut == null) {
                log.warn("警告：月结机种"+product+"没有切片数，MRP版本"+ver);
                return;
            }
            List<DemandCell> demandCellList = new ArrayList<>();
            for(BomCellMtrl bomCell : bomCellList) {
                //要属于月结机种的同个物料组继续
                if(!bomCell.getMaterialGroup().equals(materialGroup)) continue;
                DemandCell demandCell = new DemandCell();
                demandCell.setVer(ver);
                demandCell.setType(DemandLcm.TYPE_Settle);
                demandCell.setFab(fab);
                demandCell.setProduct(product);
                demandCell.setFabDate(fabDate);
                demandCell.setQty(settleQty);
                demandCell.setMaterial(bomCell.getMaterial());
                demandCell.setUsageQty(bomCell.getUsageQty());

                //替代料比例
                Double substituteRate = bomCell.getSubstituteRate();
                demandCell.setSubstituteRate(substituteRate);

                //DPS需求量 * Bon使用量 * 1000 * 切片数 * 替代比列
                //保留整数，向上取整
                if(substituteRate == null) substituteRate = 100D;
                double demandQty = DoubleUtil.upPrecision(demandCell.getQty()*demandCell.getUsageQty()*1000*cut*(substituteRate/100), 0);
                demandCell.setDemandQty(demandQty);
                demandCellList.add(demandCell);
            }
            demandService.saveDemandCell(demandCellList);
        }
    }


    @Override
    public void computeMrpMaterial(String ver) {
        String msg = "计算MRP版本"+ver+"的材料损耗率、期初库存>> ";
        log.info(msg + "START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        switch (type) {
            case MrpVer.Type_Lcm :
                computeMrpMaterialLcm(ver);
                break;
            case MrpVer.Type_Ary :
                computeMrpMaterialAry(ver);
                break;
            case MrpVer.Type_Cell :
                computeMrpMaterialCell(ver);
                break;
        }
        log.info(msg + "END");
    }


    @Override
    public void computeMrpMaterialLcm(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        java.sql.Date startDate = mrpVer.getStartDate();
        List<String> materialList = demandService.getDemandMaterialLcm(ver);
        //期初库存
        java.sql.Date inventoryDate = new java.sql.Date(DateUtil.getFirstDayOfMonth(startDate).getTime());
        List<Map> goodInventoryList = restService.getGoodInventory(fab, materialList, inventoryDate);
        List<Map> dullInventoryList = restService.getDullInventory(fab, materialList, inventoryDate);
        HashMap<String, Double> goodInventoryMap = new HashMap<>();
        HashMap<String, Double> dullInventoryMap = new HashMap<>();
        List<MrpLcmMaterial> mrpLcmMaterialList = new ArrayList<>();
        for(Map map : goodInventoryList) {
            goodInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        for(Map map : dullInventoryList) {
            dullInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        for(String material : materialList) {
            MrpLcmMaterial mrpLcmMaterial = new MrpLcmMaterial();
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
            mrpLcmMaterialList.add(mrpLcmMaterial);
        }
        mrpService.saveMrpLcmMaterial(mrpLcmMaterialList);
    }

    @Override
    public void computeMrpMaterialAry(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        java.sql.Date startDate = mrpVer.getStartDate();
        List<String> materialList = demandService.getDemandMaterialAry(ver);
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
        List<MrpAryMaterial> mrpAryMaterialList = new ArrayList<>();
        for(String material : materialList) {
            MrpAryMaterial mrpAryMaterial = new MrpAryMaterial();
            mrpAryMaterial.setVer(ver);
            mrpAryMaterial.setFab(fab);
            mrpAryMaterial.setMaterial(material);

            //损耗率
            double lossRate = lossRateService.getLossRate(material);
            mrpAryMaterial.setLossRate(lossRate);

            //期初库存
            Double goodInventory = goodInventoryMap.get(material);
            Double dullInventory = dullInventoryMap.get(material);
            mrpAryMaterial.setGoodInventory(goodInventory == null ? 0 : goodInventory);
            mrpAryMaterial.setDullInventory(dullInventory == null ? 0 : dullInventory);
            mrpAryMaterialList.add(mrpAryMaterial);
        }
        mrpService.saveMrpAryMaterial(mrpAryMaterialList);
    }

    @Override
    public void computeMrpMaterialCell(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        java.sql.Date startDate = mrpVer.getStartDate();
        List<String>materialList = demandService.getDemandMaterialCell(ver);
        //期初库存
        List<Map> goodInventoryList = restService.getGoodInventory(fab, materialList, startDate);
        List<Map> dullInventoryList = restService.getDullInventory(fab, materialList, startDate);
        HashMap<String, Double> goodInventoryMap = new HashMap<>();
        HashMap<String, Double> dullInventoryMap = new HashMap<>();
        for(Map map : goodInventoryList) {
            goodInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        for(Map map : dullInventoryList) {
            dullInventoryMap.put((String)map.get("MATERIAL"), ((BigDecimal)map.get("QTY")).doubleValue());
        }
        List<MrpCellMaterial> mrpCellMaterialList = new ArrayList<>();
        for(String material : materialList) {
            MrpCellMaterial mrpCellMaterial = new MrpCellMaterial();
            mrpCellMaterial.setVer(ver);
            mrpCellMaterial.setFab(fab);
            mrpCellMaterial.setMaterial(material);

            //损耗率
            double lossRate = lossRateService.getLossRate(material);
            mrpCellMaterial.setLossRate(lossRate);

            //期初库存
            Double goodInventory = goodInventoryMap.get(material);
            Double dullInventory = dullInventoryMap.get(material);
            mrpCellMaterial.setGoodInventory(goodInventory == null ? 0 : goodInventory);
            mrpCellMaterial.setDullInventory(dullInventory == null ? 0 : dullInventory);
            mrpCellMaterialList.add(mrpCellMaterial);
        }
        mrpService.saveMrpCellMaterial(mrpCellMaterialList);
    }

    @Override
    public void computeMrpBalance(String ver) {
        log.info("计算MRP结余量"+ver+">> START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        switch (type) {
            case MrpVer.Type_Lcm :
                computeMrpBalanceLcm(ver);
                break;
            case MrpVer.Type_Ary :
                computeMrpBalanceAry(ver);
                break;
            case MrpVer.Type_Cell :
                computeMrpBalanceCell(ver);
                break;
        }
        log.info("计算MRP结余量"+ver+">> END");
    }

    @Override
    public void computeMrpBalanceLcm(String ver) {
        List<String> materialLcmList = mrpService.getMaterialLcm(ver);
        long l = materialLcmList.size();
        for(String material : materialLcmList) {
            log.info("计算结余量料号" + material + ",剩余" + l--);
            computeMrpBalanceLcm(ver, material);
        }
    }

    @Override
    public void computeMrpBalanceLcm(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        MrpLcmMaterial mrpLcmMaterial = mrpService.getMrpLcmMaterial(ver, material);
        //损耗率
        double lossRate = mrpLcmMaterial.getLossRate();
        //期初库存
        double goodInventory = mrpLcmMaterial.getGoodInventory();
        //每日MRP
        List<MrpLcm> mrpLcmList = mrpService.getMrpLcm(ver, material);
        List<java.sql.Date> dateList = mrpService.getMrpCalendar(ver);
        if(mrpLcmList == null || mrpLcmList.size()==0) {
            mrpLcmList = new ArrayList<>();
            for(java.sql.Date fabDate : dateList) {
                MrpLcm mrpLcm = new MrpLcm();
                mrpLcm.setFab(fab);
                mrpLcm.setVer(ver);
                mrpLcm.setMaterial(material);
                mrpLcm.setFabDate(fabDate);
                mrpLcmList.add(mrpLcm);
            }
        }
        //需求量
        Map<java.sql.Date, Double> demandQtyMap = demandService.getDemandQtyLcm(ver, material);
        //到货量
        Map<java.sql.Date, Double> arrivalQtyMap = arrivalPlanService.getArrivalPlanQty(fab, material, dateList);
        //分配量
        Map<java.sql.Date, Double> allocationQtyMap = allocationService.getAllocation(fab, material, dateList);

        for(int i = 0; i<mrpLcmList.size(); i++) {
            MrpLcm mrpLcm = mrpLcmList.get(i);
            java.sql.Date fabDate = mrpLcm.getFabDate();

            //需求量
            double demandQty;
            if(demandQtyMap.get(fabDate) == null) {
                demandQty = 0;
            } else {
                demandQty = demandQtyMap.get(fabDate);
            }
            //当日耗损量
            double lossQty = DoubleUtil.upPrecision(demandQty*lossRate/100, 0);

            // 到货量
            double arrivalQty;
            if(arrivalQtyMap.get(fabDate) == null) {
                arrivalQty = 0;
            } else {
                arrivalQty = arrivalQtyMap.get(fabDate);
            }

            // 结余量
            double balanceQty;
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                balanceQty = DoubleUtil.upPrecision(goodInventory-demandQty-lossQty, 0);

            } else {
                // 其他天：前一天的结余量 + 前一天的到货 – 当日需求量 - 当日耗损量
                //前一天结余、到货
                MrpLcm lastMrpLcm = mrpLcmList.get(i-1);
                double lastBalanceQty = lastMrpLcm.getBalanceQty();
                double lastArrivalQty = lastMrpLcm.getArrivalQty();
                balanceQty =  DoubleUtil.upPrecision(lastBalanceQty + lastArrivalQty - demandQty - lossQty, 0);
            }
            //判断结余量是否修改
            if(mrpLcm.isModifyBalanceFlag()) {
                mrpLcm.setBalanceQtyHis(balanceQty);
                balanceQty = mrpLcm.getBalanceQty();
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
                    MrpLcm lastMrpLcm = mrpLcmList.get(i-1);
                    double lastBalanceQty = lastMrpLcm.getBalanceQty();
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

            mrpLcm.setDemandQty(demandQty);
            mrpLcm.setLossQty(lossQty);
            mrpLcm.setArrivalQty(arrivalQty);
            mrpLcm.setBalanceQty(balanceQty);
            mrpLcm.setShortQty(shortQty);
            mrpLcm.setAllocationQty(allocationQty);
        }
        mrpService.saveMrpLcm(mrpLcmList);
    }

    @Override
    public void computeMrpBalanceAry(String ver) {
        List<String> materialAryList = mrpService.getMaterialAry(ver);
        long l = materialAryList.size();
        for(String material : materialAryList) {
            log.info("计算结余量料号" + material + ",剩余" + l--);
            computeMrpBalanceAry(ver, material);
        }
    }

    @Override
    public void computeMrpBalanceAry(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        MrpAryMaterial mrpAryMaterial = mrpService.getMrpAryMaterial(ver, material);
        //损耗率
        double lossRate = mrpAryMaterial.getLossRate();
        //期初库存
        double goodInventory = mrpAryMaterial.getGoodInventory();
        //每日MRP
        List<MrpAry> mrpAryList = mrpService.getMrpAry(ver, material);
        List<java.sql.Date> dateList = mrpService.getMrpCalendar(ver);
        if(mrpAryList == null || mrpAryList.size()==0) {
            mrpAryList = new ArrayList<>();
            for(java.sql.Date fabDate : dateList) {
                MrpAry mrpAry = new MrpAry();
                mrpAry.setFab(fab);
                mrpAry.setVer(ver);
                mrpAry.setMaterial(material);
                mrpAry.setFabDate(fabDate);
                mrpAryList.add(mrpAry);
            }
        }
        //需求量
        Map<java.sql.Date, Double> demandQtyMap = demandService.getDemandQtyAry(ver, material);
        //到货量
        Map<java.sql.Date, Double> arrivalQtyMap = arrivalPlanService.getArrivalPlanQty(fab, material, dateList);
        //分配量
        Map<java.sql.Date, Double> allocationQtyMap = allocationService.getAllocation(fab, material, dateList);

        for(int i=0; i<mrpAryList.size(); i++) {
            MrpAry mrpAry = mrpAryList.get(i);
            java.sql.Date fabDate = mrpAry.getFabDate();

            //需求量
            double demandQty;
            if(demandQtyMap.get(fabDate) == null) {
                demandQty = 0;
            } else {
                demandQty = demandQtyMap.get(fabDate);
            }
            //当日耗损量
            double lossQty = DoubleUtil.upPrecision(demandQty*lossRate/100, 0);

            // 到货量
            double arrivalQty;
            if(arrivalQtyMap.get(fabDate) == null) {
                arrivalQty = 0;
            } else {
                arrivalQty = arrivalQtyMap.get(fabDate);
            }

            // 结余量
            double balanceQty;
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                balanceQty = DoubleUtil.upPrecision(goodInventory-demandQty-lossQty, 0);

            } else {
                // 其他天：前一天的结余量 + 前一天的到货 – 当日需求量 - 当日耗损量
                //前一天结余、到货
                MrpAry lastMrpAry = mrpAryList.get(i-1);
                double lastBalanceQty = lastMrpAry.getBalanceQty();
                double lastArrivalQty = lastMrpAry.getArrivalQty();
                balanceQty =  DoubleUtil.upPrecision(lastBalanceQty + lastArrivalQty - demandQty - lossQty, 0);
            }
            //判断结余量是否修改
            if(mrpAry.isModifyBalanceFlag()) {
                mrpAry.setBalanceQtyHis(balanceQty);
                balanceQty = mrpAry.getBalanceQty();
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
                    MrpAry lastMrpAry = mrpAryList.get(i-1);
                    double lastBalanceQty = lastMrpAry.getBalanceQty();
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

            mrpAry.setDemandQty(demandQty);
            mrpAry.setLossQty(lossQty);
            mrpAry.setArrivalQty(arrivalQty);
            mrpAry.setBalanceQty(balanceQty);
            mrpAry.setShortQty(shortQty);
            mrpAry.setAllocationQty(allocationQty);
        }
        mrpService.saveMrpAry(mrpAryList);
    }

    @Override
    public void computeMrpBalanceCell(String ver) {
        List<String> materialCellList = mrpService.getMaterialCell(ver);
        long l = materialCellList.size();
        for(String material : materialCellList) {
            log.info("计算结余量料号" + material + ",剩余" + l--);
            computeMrpBalanceCell(ver, material);
        }
    }

    @Override
    public void computeMrpBalanceCell(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        MrpCellMaterial mrpCellMaterial = mrpService.getMrpCellMaterial(ver, material);
        //损耗率
        double lossRate = mrpCellMaterial.getLossRate();
        //期初库存
        double goodInventory = mrpCellMaterial.getGoodInventory();

        //每日MRP
        List<MrpCell> mrpCellList = mrpService.getMrpCell(ver, material);
        List<java.sql.Date> dateList = mrpService.getMrpCalendar(ver);
        if(mrpCellList == null || mrpCellList.size()==0) {
            mrpCellList = new ArrayList<>();
            for(java.sql.Date fabDate : dateList) {
                MrpCell mrpCell = new MrpCell();
                mrpCell.setFab(fab);
                mrpCell.setVer(ver);
                mrpCell.setMaterial(material);
                mrpCell.setFabDate(fabDate);
                mrpCellList.add(mrpCell);
            }
        }

        //需求量
        Map<java.sql.Date, Double> demandQtyMap = demandService.getDemandQtyCell(ver, material);
        //到货量
        Map<java.sql.Date, Double> arrivalQtyMap = arrivalPlanService.getArrivalPlanQty(fab, material, dateList);
        //分配量
        Map<java.sql.Date, Double> allocationQtyMap = allocationService.getAllocation(fab, material, dateList);

        for(int i=0; i<mrpCellList.size(); i++) {
            MrpCell mrpCell = mrpCellList.get(i);
            java.sql.Date fabDate = mrpCell.getFabDate();

            //需求量
            double demandQty;
            if(demandQtyMap.get(fabDate) == null) {
                demandQty = 0;
            } else {
                demandQty = demandQtyMap.get(fabDate);
            }
            //当日耗损量
            double lossQty = DoubleUtil.upPrecision(demandQty*lossRate/100, 0);

            // 到货量
            double arrivalQty;
            if(arrivalQtyMap.get(fabDate) == null) {
                arrivalQty = 0;
            } else {
                arrivalQty = arrivalQtyMap.get(fabDate);
            }

            // 结余量
            double balanceQty;
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                balanceQty = DoubleUtil.upPrecision(goodInventory-demandQty-lossQty, 0);

            } else {
                // 其他天：前一天的结余量 + 前一天的到货 – 当日需求量 - 当日耗损量
                //前一天结余、到货
                MrpCell lastMrpCell = mrpCellList.get(i-1);
                double lastBalanceQty = lastMrpCell.getBalanceQty();
                double lastArrivalQty = lastMrpCell.getArrivalQty();
                balanceQty =  DoubleUtil.upPrecision(lastBalanceQty + lastArrivalQty - demandQty - lossQty, 0);
            }
            //判断结余量是否修改
            if(mrpCell.isModifyBalanceFlag()) {
                mrpCell.setBalanceQtyHis(balanceQty);
                balanceQty = mrpCell.getBalanceQty();
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
                    MrpCell lastMrpCell = mrpCellList.get(i-1);
                    double lastBalanceQty = lastMrpCell.getBalanceQty();
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

            mrpCell.setDemandQty(demandQty);
            mrpCell.setLossQty(lossQty);
            mrpCell.setArrivalQty(arrivalQty);
            mrpCell.setBalanceQty(balanceQty);
            mrpCell.setShortQty(shortQty);
            mrpCell.setAllocationQty(allocationQty);
        }

        mrpService.saveMrpCell(mrpCellList);
    }

    @Override
    public void computeMrpBalance(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        switch (type) {
            case MrpVer.Type_Lcm :
                computeMrpBalanceLcm(ver, material);
                break;
            case MrpVer.Type_Ary :
                computeMrpBalanceAry(ver, material);
                break;
            case MrpVer.Type_Cell :
                computeMrpBalanceCell(ver, material);
                break;
        }
    }


    @Override
    public void runMrp(String[] dpsVers, String[] mpsVers, String user) {
        MrpVer mrpVer = createMrpVer(dpsVers, mpsVers, user);
        String type = mrpVer.getType();
        String ver = mrpVer.getVer();
        if(StringUtils.equalsAny(type, MrpVer.Type_Lcm, MrpVer.Type_Ary, MrpVer.Type_Cell)) {
            computeDemand(ver);
            computeMrpMaterial(ver);
            computeMrpBalance(ver);
        }
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
            mrpPackageBox.setVer(ver);
            mrpPackageBox.setProduct(product);
            mrpPackageBox.setType(type);
            mrpPackageBox.setLinkQty(linkQty);
            mrpPackageBox.setMode(mode);
            mrpPackageBox.setMaterial(box.getMaterial());
            mrpPackageBox.setMaterialName(box.getMaterialName());
            mrpPackageBox.setMaterialGroup(box.getMaterialGroup());
            mrpPackageBox.setMaterialGroup(box.getMaterialGroupName());
            mrpPackageBox.setDemandQty(box_qty);
            mrpPackageBox.setArrivalQty(arrivalQty_box);
            mrpPackageBox.setAllocationQty(allocationQty_box);

            MrpPackage mrpPackageTray = new MrpPackage();
            mrpPackageTray.setVer(ver);
            mrpPackageTray.setProduct(product);
            mrpPackageTray.setType(type);
            mrpPackageTray.setLinkQty(linkQty);
            mrpPackageTray.setMode(mode);
            mrpPackageTray.setMaterial(tray.getMaterial());
            mrpPackageTray.setMaterialName(tray.getMaterialName());
            mrpPackageTray.setMaterialGroup(tray.getMaterialGroup());
            mrpPackageTray.setMaterialGroup(tray.getMaterialGroupName());
            mrpPackageTray.setDemandQty(tray_qty);
            mrpPackageTray.setArrivalQty(arrivalQty_tray);
            mrpPackageTray.setAllocationQty(allocationQty_tray);

            mrpPackageList.add(mrpPackageBox);
            mrpPackageList.add(mrpPackageTray);
        }
        mrpService.saveMrpPackage(mrpPackageList);
    }

    @Override
    public void completeMrpMaterial(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        if(MrpVer.Type_Lcm.equals(mrpVer.getType())) {
            List<MrpLcmMaterial> materialList = mrpService.getMrpLcmMaterial(ver);
            for(MrpLcmMaterial mrpLcmMaterial : materialList) {
                String material = mrpLcmMaterial.getMaterial();
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

                List<String> productList = demandService.getDemandProductLcm(ver, material);
                StringBuffer productStr = new StringBuffer();
                for(String product : productList) {
                    if(StringUtils.isNotEmpty(productStr)) {
                        productStr.append(",");
                    }
                    if(StringUtils.isNotEmpty(product)) {
                        productStr.append(product);
                    }
                }

                mrpLcmMaterial.setMaterialName(materialName);
                mrpLcmMaterial.setMaterialGroup(materialGroup);
                mrpLcmMaterial.setMaterialGroupName(materialGroupName);
                mrpLcmMaterial.setSupplierCodes(supplierCodeStr.toString());
                mrpLcmMaterial.setSuppliers(supplierStr.toString());
                mrpLcmMaterial.setProducts(productStr.toString());
            }
            mrpService.saveMrpLcmMaterial(materialList);
        } else if(MrpVer.Type_Ary.equals(mrpVer.getType())) {
            List<MrpAryMaterial> materialList = mrpService.getMrpAryMaterial(ver);
            for(MrpAryMaterial mrpAryMaterial : materialList) {
                String material = mrpAryMaterial.getMaterial();
                String materialName = materialService.getMaterialName(material);
                String materialGroup = materialService.getMaterialGroup(material);
                String materialGroupName = materialGroupService.getMaterialGroupName(materialGroup);

                List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
                StringBuffer supplierCodeStr = new StringBuffer();
                StringBuffer supplierStr = new StringBuffer();
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

                List<String> productList = demandService.getDemandProductAry(ver, material);
                StringBuffer productStr = new StringBuffer();
                for(String product : productList) {
                    if(StringUtils.isNotEmpty(productStr)) {
                        productStr.append(",");
                    }
                    if(StringUtils.isNotEmpty(product)) {
                        productStr.append(product);
                    }
                }

                mrpAryMaterial.setMaterialName(materialName);
                mrpAryMaterial.setMaterialGroup(materialGroup);
                mrpAryMaterial.setMaterialGroupName(materialGroupName);
                mrpAryMaterial.setSupplierCodes(supplierCodeStr.toString());
                mrpAryMaterial.setSuppliers(supplierStr.toString());
                mrpAryMaterial.setProducts(productStr.toString());
            }
            mrpService.saveMrpAryMaterial(materialList);
        } else if(MrpVer.Type_Cell.equals(mrpVer.getType())) {
            List<MrpCellMaterial> materialList = mrpService.getMrpCellMaterial(ver);
            for(MrpCellMaterial mrpCellMaterial : materialList) {
                String material = mrpCellMaterial.getMaterial();
                String materialName = materialService.getMaterialName(material);
                String materialGroup = materialService.getMaterialGroup(material);
                String materialGroupName = materialGroupService.getMaterialGroupName(materialGroup);

                List<Supplier> supplierList = supplierService.getSupplierByMaterial(material);
                StringBuffer supplierCodeStr = new StringBuffer();
                StringBuffer supplierStr = new StringBuffer();
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

                List<String> productList = demandService.getDemandProductCell(ver, material);
                StringBuffer productStr = new StringBuffer();
                for(String product : productList) {
                    if(StringUtils.isNotEmpty(productStr)) {
                        productStr.append(",");
                    }
                    if(StringUtils.isNotEmpty(product)) {
                        productStr.append(product);
                    }
                }

                mrpCellMaterial.setMaterialName(materialName);
                mrpCellMaterial.setMaterialGroup(materialGroup);
                mrpCellMaterial.setMaterialGroupName(materialGroupName);
                mrpCellMaterial.setSupplierCodes(supplierCodeStr.toString());
                mrpCellMaterial.setSuppliers(supplierStr.toString());
                mrpCellMaterial.setProducts(productStr.toString());
            }
            mrpService.saveMrpCellMaterial(materialList);
        }

    }


    @Override
    public void updateMrpMaterial(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        switch (type) {
            case MrpVer.Type_Lcm :
                computeMrpBalanceLcm(ver, material);
                break;
            case MrpVer.Type_Ary :
                computeMrpBalanceAry(ver, material);
                break;
            case MrpVer.Type_Cell :
                computeMrpBalanceCell(ver, material);
                break;
        }
    }

    @Override
    public void updateMrpBalanceQty(String ver, String material, java.sql.Date fabDate, double balanceQty) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        if(MrpVer.Type_Lcm.equals(type)) {
            MrpLcm mrpLcm = mrpService.getMrpLcm(ver, material, fabDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(mrpLcm.isModifyBalanceFlag()) {
                mrpLcm.setBalanceQty(balanceQty);
                mrpLcm.setMemo("结余量修改"+sdf.format(new Date()));
            } else {
                mrpLcm.setModifyBalanceFlag(true);
                mrpLcm.setBalanceQtyHis(mrpLcm.getBalanceQty());
                mrpLcm.setBalanceQty(balanceQty);
                mrpLcm.setMemo("结余量修改"+sdf.format(new Date()));
            }
            List<MrpLcm> list = new ArrayList<>();
            list.add(mrpLcm);
            mrpService.saveMrpLcm(list);
        } else if(MrpVer.Type_Cell.equals(type)) {
            MrpCell mrpCell = mrpService.getMrpCell(ver, material, fabDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(mrpCell.isModifyBalanceFlag()) {
                mrpCell.setBalanceQty(balanceQty);
                mrpCell.setMemo("结余量修改"+sdf.format(new Date()));
            } else {
                mrpCell.setModifyBalanceFlag(true);
                mrpCell.setBalanceQtyHis(mrpCell.getBalanceQty());
                mrpCell.setBalanceQty(balanceQty);
                mrpCell.setMemo("结余量修改"+sdf.format(new Date()));
            }
            List<MrpCell> list = new ArrayList<>();
            list.add(mrpCell);
            mrpService.saveMrpCell(list);
        } else if(MrpVer.Type_Ary.equals(type)) {
            MrpAry mrpAry = mrpService.getMrpAry(ver, material, fabDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(mrpAry.isModifyBalanceFlag()) {
                mrpAry.setBalanceQty(balanceQty);
                mrpAry.setMemo("结余量修改"+sdf.format(new Date()));
            } else {
                mrpAry.setModifyBalanceFlag(true);
                mrpAry.setBalanceQtyHis(mrpAry.getBalanceQty());
                mrpAry.setBalanceQty(balanceQty);
                mrpAry.setMemo("结余量修改"+sdf.format(new Date()));
            }
            List<MrpAry> list = new ArrayList<>();
            list.add(mrpAry);
            mrpService.saveMrpAry(list);
        }
    }
}
