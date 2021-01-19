package com.ivo.mrp.service.ary;

import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.mrp.entity.*;
import com.ivo.mrp.entity.direct.ary.*;
import com.ivo.mrp.entity.direct.cell.*;
import com.ivo.mrp.entity.direct.lcm.*;
import com.ivo.mrp.exception.MrpException;
import com.ivo.mrp.service.*;
import com.ivo.mrp.service.cell.BomCellService;
import com.ivo.rest.RestService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wj
 * @version 1.0
 */
@Slf4j
@Service
public class RunMrpAryServiceImpl implements RunMrpAryService {

    private DpsService dpsService;
    private MpsService mpsService;
    private MrpService mrpService;
    private MonthSettleService monthSettleService;
    private DemandService demandService;
    private CutService cutService;
    private LossRateService lossRateService;
    private RestService restService;
    private ArrivalPlanService arrivalPlanService;
    private AllocationService allocationService;
    private MaterialService materialService;
    private MaterialGroupService materialGroupService;
    private SupplierService supplierService;
    private ActualArrivalService actualArrivalService;
    private MrpWarnService mrpWarnService;

    private BomAryService bomAryService;

    @Autowired
    public RunMrpAryServiceImpl(DpsService dpsService, MpsService mpsService, MrpService mrpService,
                             MonthSettleService monthSettleService, BomService bomService,
                             DemandService demandService,
                             CutService cutService,
                             LossRateService lossRateService,
                             RestService restService,
                             ArrivalPlanService arrivalPlanService,
                             AllocationService allocationService,
                             MaterialService materialService,
                             MaterialGroupService materialGroupService,
                             SupplierService supplierService,
                             ActualArrivalService actualArrivalService,
                             MrpWarnService mrpWarnService, BomCellService bomCellService,
                             BomAryService bomAryService) {
        this.dpsService = dpsService;
        this.mpsService = mpsService;
        this.mrpService = mrpService;
        this.monthSettleService = monthSettleService;
        this.demandService = demandService;
        this.cutService = cutService;
        this.lossRateService = lossRateService;
        this.restService = restService;
        this.arrivalPlanService = arrivalPlanService;
        this.allocationService = allocationService;
        this.materialService = materialService;
        this.materialGroupService = materialGroupService;
        this.supplierService = supplierService;
        this.actualArrivalService = actualArrivalService;
        this.mrpWarnService = mrpWarnService;
        this.bomAryService = bomAryService;
    }

    @Override
    public MrpVer createMrpVer(String[] dpsVers, String[] mpsVers, String user) {
        MrpVer mrpVer = checkDpsAndMps(dpsVers, mpsVers);
        String ver = generateMpsVer();
        log.info("生成MRP版本" + ver);
        mrpVer.setVer(ver);
        mrpVer.setCreator(user);
        mrpVer.setMemo("运算中");
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
        Set<String> dpsVerSet = new HashSet<>();
        List<Date[]> dps_dateList = new ArrayList<>();
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
            dps_dateList.add(dates);
        }
        Set<String> mpsVerSet = new HashSet<>();
        List<java.sql.Date[]> mps_dateList = new ArrayList<>();
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
            mps_dateList.add(dates);
        }

        if(!StringUtils.equalsAny(fab, "LCM1", "LCM2", "CELL", "ARY"))
            throw new MrpException("DPS&MPS版本的厂别"+fab+"不正确");
        if(!StringUtils.equalsAny(type, "LCM", "ARY", "CELL", "包材"))
            throw new MrpException("DPS&MPS版本的类型"+type+"不正确");

        java.sql.Date[] dps_dates = checkRangeDate(dps_dateList.toArray(new java.sql.Date[dps_dateList.size()][2]));
        if(mps_dateList.size() == 0) mps_dateList = dps_dateList;
        java.sql.Date[] mps_dates = checkRangeDate(mps_dateList.toArray(new java.sql.Date[mps_dateList.size()][2]));
        MrpVer mrpVer = new MrpVer();
        assert fab != null;
        mrpVer.setFab(fab);

        //每月第一天
        java.sql.Date startDate = new java.sql.Date(DateUtil.getFirstDayOfMonth(dps_dates[0]).getTime());
        java.sql.Date nowDate =  new java.sql.Date(DateUtil.getFirstDayOfMonth(new java.sql.Date(new java.util.Date().getTime())).getTime());
        //如果开始时间在当月之后，以当月为准
        if(!startDate.equals(nowDate)) {
            startDate = nowDate;
        }
        java.sql.Date endDate = mps_dates[1];
        if(startDate.after(endDate)) {
            throw new MrpException("DPS&MPS版本的日期不正确");
        }
        mrpVer.setStartDate(startDate);
        mrpVer.setEndDate(endDate);
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
        computeDpsDemandAry(ver, dpsVer);
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
    public void computeDpsDemand(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        String type = dps.getType();
        computeDpsDemandAry(ver, dpsVer, product);
        computeDpsDemandAryOc(ver, dpsVer, product);
    }

    @Override
    public void computeDpsDemandAry(String ver, String dpsVer, String product) {
        DpsVer dps = dpsService.getDpsVer(dpsVer);
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = dps.getFab();
        List<BomAry> bomAryList = bomAryService.getBomAry(product);
        if(bomAryList == null || bomAryList.size()==0) {
            log.warn("警告：DPS机种"+product+"没有BOM List，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "DPS", "没有BOM");
            return;
        }
        String project = StringUtils.contains(product, "") ? product.substring(0, product.indexOf(" ")) : product;
        Double cut = cutService.getProjectCut(project);
        if(cut == null) {
            log.warn("警告：DPS机种"+product+"没有切片数，MRP版本"+ver);
            return;
        }
        List<DpsAry> dpsAryList = dpsService.getDpsAry(dpsVer, product, mrpVer.getStartDate());
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
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = dps.getFab();
        List<Map> bomOcList = bomAryService.getBomAryOc(product);
        if(bomOcList == null || bomOcList.size()==0) {
            log.warn("警告：DPS机种"+product+" ARY 2次Input没有OC BOM List，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "DPS", "ARY 2次Input没有BOM");
            return;
        }
        String project = StringUtils.contains(product, "") ? product.substring(0, product.indexOf(" ")) : product;
        Double cut = cutService.getProjectCut(project);
        if(cut == null) {
            log.warn("警告：DPS机种"+product+"没有切片数，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "DPS", "没有切片数");
            return;
        }
        List<DpsAryOc> dpsAryOcList = dpsService.getDpsAryOc(dpsVer, product, mrpVer.getStartDate());
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
    public void computeMpsDemand(String ver, String mpsVer) {
        String msg = "计算MRP版本"+ver+"的需求量>> MPS" + mpsVer ;
        log.info(msg);
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String type = mps.getType();
        computeMpsDemandAry(ver, mpsVer);
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
    public void computeMpsDemand(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String type = mps.getType();
        computeMpsDemandAry(ver, mpsVer, product);
    }

    @Override
    public void computeMpsDemandAry(String ver, String mpsVer, String product) {
        MpsVer mps = mpsService.getMpsVer(mpsVer);
        String fab = mps.getFab();
        List<BomAry> bomAryList = bomAryService.getBomAry(product);
        if(bomAryList == null || bomAryList.size()==0) {
            log.warn("警告：MPS机种"+product+"没有BOM List，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "MPS", "没有BOM");
            return;
        }
        String project = StringUtils.contains(product, " ") ? product.substring(0, product.indexOf(" ")) : product;
        Double cut = cutService.getProjectCut(project);
        if(cut == null) {
            log.warn("警告：MPS机种"+product+"没有切片数，MRP版本"+ver);
            mrpWarnService.addWarn(ver, product, "MPS", "没有切片数");
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


        List<MpsAry> mpsAryList = mpsService.getMpsAry(mpsVer, product, startDate);
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
    public void computeSettleDemand(String ver) {
        log.info("计算MRP版本"+ver+"的需求量>> 月结");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String fab = mrpVer.getFab();
        java.sql.Date startDate = mrpVer.getStartDate();
        String month = DateUtil.getLastMonth(startDate);
        String type = mrpVer.getType();
        computeSettleDemandAry(ver, fab, month);
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
            List<BomAry> bomAryList = bomAryService.getBomAry(product);
            if(bomAryList == null || bomAryList.size()==0) {
                log.warn("警告：月结机种"+product+"没有BOM List，MRP版本"+ver);
                mrpWarnService.addWarn(ver, product, "月结", "没有BOM");
                return;
            }
            String project = StringUtils.contains(product, "") ? product.substring(0, product.indexOf(" ")) : product;
            Double cut = cutService.getProjectCut(project);
            if(cut == null) {
                log.warn("警告：月结机种"+product+"没有切片数，MRP版本"+ver);
                mrpWarnService.addWarn(ver, product, "月结", "没有切片数");
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
    public void computeMrpMaterial(String ver) {
        String msg = "计算MRP版本"+ver+"的材料损耗率、期初库存>> ";
        log.info(msg + "START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        computeMrpMaterialAry(ver);
        log.info(msg + "END");
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
            goodInventoryMap.put((String)map.get("MATERIAL"), (double)map.get("QTY"));
        }
        for(Map map : dullInventoryList) {
            dullInventoryMap.put((String)map.get("MATERIAL"), (double)map.get("QTY"));
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
            mrpAryMaterial.setInventorDate(inventoryDate);
            mrpAryMaterialList.add(mrpAryMaterial);
        }
        mrpService.saveMrpAryMaterial(mrpAryMaterialList);
    }

    @Override
    public void computeMrpBalance(String ver) {
        log.info("计算MRP结余量"+ver+">> START");
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        computeMrpBalanceAry(ver);
        log.info("计算MRP结余量"+ver+">> END");
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
        Map<java.sql.Date, Double> allocationQtyMap = allocationService.getAllocationQty(fab, material, dateList);

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
            double lossQty = DoubleUtil.upPrecision(demandQty*lossRate, 0);

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
            mrpAry.setArrivalActualQty(arrivalActualQty);
            mrpAry.setArrivalPlanQty(arrivalPlanQty);
        }
        mrpService.saveMrpAry(mrpAryList);
    }

    @Override
    public void computeMrpBalance(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        computeMrpBalanceAry(ver, material);
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
            completeMrpMaterial(ver);
        }
        mrpVer.setMemo("运算完成");
        mrpService.saveMrpVer(mrpVer);
    }

    @Override
    public void completeMrpMaterial(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        log.info("MRP计算同步物料信息>> START");
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
        log.info("MRP计算同步物料信息>> END");
    }


    @Override
    public void updateMrpMaterial(String ver, String material) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        computeMrpBalanceAry(ver, material);
    }

    @Override
    public void updateMrpBalanceQty(String ver, String material, java.sql.Date fabDate, double balanceQty) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        String type = mrpVer.getType();
        if(MrpVer.Type_Lcm.equals(type)) {
            MrpLcm mrpLcm = mrpService.getMrpLcm(ver, material, fabDate);
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

            List<MrpLcm> list = new ArrayList<>();
            list.add(mrpLcm);
            mrpService.saveMrpLcm(list);
        } else if(MrpVer.Type_Cell.equals(type)) {
            MrpCell mrpCell = mrpService.getMrpCell(ver, material, fabDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(mrpCell.getBalanceQtyHis() == balanceQty) {
                mrpCell.setModifyBalanceFlag(false);
                mrpCell.setBalanceQty(balanceQty);
            } else {
                if(mrpCell.isModifyBalanceFlag()) {
                    mrpCell.setBalanceQty(balanceQty);
                    mrpCell.setMemo("结余量修改"+sdf.format(new java.util.Date()));
                } else {
                    mrpCell.setModifyBalanceFlag(true);
                    mrpCell.setBalanceQtyHis(mrpCell.getBalanceQty());
                    mrpCell.setBalanceQty(balanceQty);
                    mrpCell.setMemo("结余量修改"+sdf.format(new java.util.Date()));
                }
            }

            List<MrpCell> list = new ArrayList<>();
            list.add(mrpCell);
            mrpService.saveMrpCell(list);
        } else if(MrpVer.Type_Ary.equals(type)) {
            MrpAry mrpAry = mrpService.getMrpAry(ver, material, fabDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if(mrpAry.getBalanceQtyHis() == balanceQty) {
                mrpAry.setModifyBalanceFlag(false);
                mrpAry.setBalanceQty(balanceQty);
            } else {
                if(mrpAry.isModifyBalanceFlag()) {
                    mrpAry.setBalanceQty(balanceQty);
                    mrpAry.setMemo("结余量修改"+sdf.format(new java.util.Date()));
                } else {
                    mrpAry.setModifyBalanceFlag(true);
                    mrpAry.setBalanceQtyHis(mrpAry.getBalanceQty());
                    mrpAry.setBalanceQty(balanceQty);
                    mrpAry.setMemo("结余量修改"+sdf.format(new java.util.Date()));
                }
            }

            List<MrpAry> list = new ArrayList<>();
            list.add(mrpAry);
            mrpService.saveMrpAry(list);
        }
    }

    @Override
    public double updateMrpAllocationQty(String ver, String material, java.sql.Date fabDate) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        double allocationQty = allocationService.getAllocationQty(mrpVer.getFab(), material, fabDate);
        String type = mrpVer.getType();
        if(MrpVer.Type_Lcm.equals(type)) {
            MrpLcm mrpLcm = mrpService.getMrpLcm(ver, material, fabDate);
            List<MrpLcm> list = new ArrayList<>();
            list.add(mrpLcm);
            mrpLcm.setAllocationQty(allocationQty);
            mrpService.saveMrpLcm(list);
        } else if(MrpVer.Type_Cell.equals(type)) {
            MrpCell mrpCell = mrpService.getMrpCell(ver, material, fabDate);
            List<MrpCell> list = new ArrayList<>();
            list.add(mrpCell);
            mrpCell.setAllocationQty(allocationQty);
            mrpService.saveMrpCell(list);
        } else if(MrpVer.Type_Ary.equals(type)) {
            MrpAry mrpAry = mrpService.getMrpAry(ver, material, fabDate);
            List<MrpAry> list = new ArrayList<>();
            list.add(mrpAry);
            mrpAry.setAllocationQty(allocationQty);
            mrpService.saveMrpAry(list);
        }
        return allocationQty;
    }


    @Override
    public void updateMrp(String ver) {
        MrpVer mrpVer = mrpService.getMrpVer(ver);
        mrpVer.setMemo("更新中");
        mrpService.saveMrpVer(mrpVer);
        String type = mrpVer.getType();
        demandService.deleteDemand(ver);
        computeDemand(ver);
        computeMrpMaterialAry(ver);
        computeMrpBalanceAry(ver);
        completeMrpMaterial(ver);
        mrpVer.setMemo("更新完成");
        mrpService.saveMrpVer(mrpVer);
    }
}
