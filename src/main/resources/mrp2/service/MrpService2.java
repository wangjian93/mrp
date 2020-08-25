package com.ivo.mrp2.service;

import com.ivo.common.BatchService;
import com.ivo.common.utils.DateUtil;
import com.ivo.common.utils.DoubleUtil;
import com.ivo.common.utils.StringUtil;
import com.ivo.mrp2.entity.*;
import com.ivo.mrp2.repository.MrpDataMasterRepository;
import com.ivo.mrp2.repository.MrpDataRepository;
import com.ivo.mrp2.repository.MrpVerRepository;
import com.ivo.rest.fcst.service.FcstService;
import com.ivo.rest.oracle.service.CellBomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.*;

/**
 * MRP计算服务
 * @author wj
 * @version 1.0
 */
@Service
@Slf4j
public class MrpService2 {


    private DpsService dpsService;

    private MrpVerRepository mrpVerRepository;

    private DemandService demandService;

    private MrpDataMasterRepository mrpDataMasterRepository;

    private MrpDataRepository mrpDataRepository;

    private MaterialService materialService;

    private InventoryService inventoryService;

    private SupplierArrivalPlanService supplierArrivalPlanService;

    private MaterialSupplierService materialSupplierService;

    private MaterialLossRateService materialLossRateService;

    private BatchService batchService;

    private MrpVerService mrpVerService;

    private MrpDataService mrpDataService;

    private MrpDataMasterService mrpDataMasterService;

    private MrpAllocationService mrpAllocationService;

    private FcstService fcstService;

    private CellBomService cellBomService;

    private MaterialSubstituteService materialSubstituteService;

    private ProductSliceService productSliceService;

    private BomService bomService;

    @Autowired
    public MrpService2(DpsService dpsService, MrpVerRepository mrpVerRepository,
                       DemandService demandService, MrpDataMasterRepository mrpDataMasterRepository,
                       MrpDataRepository mrpDataRepository, MaterialService materialService,
                       InventoryService inventoryService, SupplierArrivalPlanService supplierArrivalPlanService,
                       MaterialSupplierService materialSupplierService, MaterialLossRateService materialLossRateService,
                       BatchService batchService,
                       MrpVerService mrpVerService,
                       MrpDataService mrpDataService,
                       MrpDataMasterService mrpDataMasterService,
                       MrpAllocationService mrpAllocationService,
                       FcstService fcstService,
                       CellBomService cellBomService,
                       MaterialSubstituteService materialSubstituteService,
                       ProductSliceService productSliceService,
                       BomService bomService) {
        this.dpsService = dpsService;
        this.mrpVerRepository = mrpVerRepository;
        this.demandService = demandService;
        this.mrpDataMasterRepository = mrpDataMasterRepository;
        this.mrpDataRepository = mrpDataRepository;
        this.materialService = materialService;
        this.inventoryService = inventoryService;
        this.supplierArrivalPlanService = supplierArrivalPlanService;
        this.materialSupplierService = materialSupplierService;
        this.materialLossRateService = materialLossRateService;
        this.batchService = batchService;
        this.mrpVerService = mrpVerService;
        this.mrpDataService =  mrpDataService;
        this.mrpDataMasterService = mrpDataMasterService;
        this.mrpAllocationService = mrpAllocationService;
        this.fcstService = fcstService;
        this.cellBomService = cellBomService;
        this.materialSubstituteService = materialSubstituteService;
        this.productSliceService = productSliceService;
        this.bomService = bomService;
    }

    /**
     * 1.DPS&MPS版本检查
     * @param dpsVers DPS版本
     * @param mpsVers MPS版本
     * @return boolean
     */
    public boolean checkVer(String[] dpsVers, String[] mpsVers) {
        log.info("DPS&MPS版本检查");
        //厂别需要一致
        String plant = dpsVers[0];
        for(String ver : dpsVers) {
            if(!StringUtils.equalsIgnoreCase(dpsService.getDpsVer(ver).getFab(), plant)) return false;
        }
        return true;
    }

    /**
     * 2.创建MRP版本
     * @param dpsVers DPS版本
     * @param mpsVers MPS版本
     * @return MrpVer
     */
    public MrpVer createMrpVer(String[] dpsVers, String[] mpsVers) {
        log.info("创建MRP版本");
        MrpVer mrpVer = new MrpVer();
        String plant = dpsService.getDpsVer(dpsVers[0]).getFab();
        Date startDate = null;
        Date endDate = null;
        String dpsVerStr = null;
        for(String ver : dpsVers) {
            DpsVer dpsVer = dpsService.getDpsVer(ver);
            if(startDate == null || startDate.after(dpsVer.getStartDate())) {
                startDate = dpsVer.getStartDate();
            }
            if(endDate == null || endDate.before(dpsVer.getEndDate())) {
                endDate = dpsVer.getEndDate();
            }

            if(dpsVerStr == null)
                dpsVerStr = ver;
            else
                dpsVerStr = dpsVerStr + "," + ver;
        }
        mrpVer.setMrpVer(generateMrpVer());
        mrpVer.setPlant(plant);
        mrpVer.setStartDate(startDate);
        mrpVer.setEndDate(endDate);
        mrpVer.setDpsVer(dpsVerStr);
        mrpVer.setMpsVer(null);
        mrpVer.setStatus(MrpVer.STATUS_COMPUTE);
        mrpVer.setCreateDate(new Date(new java.util.Date().getTime()));
        mrpVerRepository.saveAndFlush(mrpVer);
        return mrpVer;
    }

    /**
     * 2.1生成MRP版本号
     * @return String
     */
    private String generateMrpVer() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new java.util.Date());
    }

    /**
     * 3.计算材料的需求
     */
    public void computeDemand(MrpVer mrpVer) throws IOException, ClassNotFoundException {
        log.info("计算材料的需求>> START");
        String[] dpsVers = mrpVer.splitDpsVer();
        for(String dpsVer : dpsVers) {
            if(demandService.isExist(dpsVer)) {
                demandService.delete(demandService.getDemand(dpsVer));
            }
            computeDemandDps(dpsVer);
            DpsVer dpsVerObj = dpsService.getDpsVer(dpsVer);
            if(StringUtils.isNotEmpty(dpsVerObj.getSmallVer())) {
                if(demandService.isExist(dpsVerObj.getSmallVer())) {
                    demandService.delete(demandService.getDemand(dpsVerObj.getSmallVer()));
                }
                computeDemandDps(dpsVerObj.getSmallVer());
            }
        }
        log.info("计算材料的需求>> END");
    }

    /**
     * 3.1计算DPS的材料的需求
     * @param ver DPS版本
     */
    public void computeDemandDps(String ver) throws IOException, ClassNotFoundException {
        log.info("展开DPS版本"+ver+">> START");
        demandService.delete(demandService.getDemand(ver));
        List<String> productList =  dpsService.getProduct(ver);
        DpsVer dpsVer = dpsService.getDpsVer(ver);
        //小版本问题BUG处理
        if(dpsVer == null) {
            dpsVer = dpsService.getDpsVer(ver.substring(0, ver.length()-1));
            dpsVer.setVer(ver);
        }
        int i = productList.size();
        for(String product : productList) {
            log.info("展开机种"+product+"剩余"+ (i--) );
            computeDemandProduct(dpsVer, product);
        }
        log.info("展开DPS版本"+ver+">> END");
    }

    /**
     * 3.2计算机种的需求
     * @param dpsVer DPS版本
     * @param product 机种
     */
    public void computeDemandProduct(DpsVer dpsVer, String product) throws IOException, ClassNotFoundException {
        String plant = dpsVer.getFab();
        List<Demand> modeList;
        if(StringUtils.equalsIgnoreCase(plant, "CELL")) {
            modeList = cellDemand(plant, product);
        } else {
            modeList = sqlHandelDemand(plant, product);
        }
        List<DpsData> dpsList = dpsService.getDpsData(dpsVer.getVer(), product);
        if(StringUtils.equalsIgnoreCase(product, "N1012")) {
            System.out.println("N1012");
        }
        for(DpsData dps : dpsList) {
            Date fabDate = dps.getFabDate();
            double dpsQty = dps.getQty();
            //使用深拷贝
            List<Demand> demandList = deepCopy(modeList);

            for(Demand demand : demandList) {
                demand.setDpsVer(dpsVer.getVer());
                demand.setFabDate(fabDate);
                demand.setDpsQty(dpsQty);

                double demandQty = dpsQty;
                // LCM ： DPS需求量 * Bon使用量 * 替代比列
                // ARY/CELL ： DPS需求量 * 1000 * 切片数 * Bon使用量 * 替代比列
                if(StringUtils.equalsAnyIgnoreCase(plant, "CELL", "ARY")) {
                    demandQty = DoubleUtil.multiply(demandQty , demand.getUsageQty(), demand.getSlice(), 1000d);
                } else {
                    demandQty = DoubleUtil.multiply(demandQty , demand.getUsageQty());
                }
                //替代料
                if(demand.getSubstituteRate() != null) {
                    demandQty = DoubleUtil.multiply(demandQty, demand.getSubstituteRate()/100);
                }

                //ARY/CELL保留三位小数，LCM保留整数，向上取整
                if(StringUtils.equalsAnyIgnoreCase(plant, "CELL", "ARY")) {
                    demandQty = DoubleUtil.upPrecision(demandQty, 0);
                } else {
                    demandQty = DoubleUtil.upPrecision(demandQty, 0);
                }
                demand.setDemandQty(demandQty);
            }
            demandService.batchSave(demandList);
        }
    }

    /**
     * 3.3展开BOM
     * 3.4替代料处理
     * 3.5CELL切片数
     * @param plant 厂别
     * @param product  机种
     * @return List<Demand>
     */
    public List<Demand> sqlHandelDemand(String plant, String product) {
        List<Map> mapList = mrpVerRepository.sqlHandelDemand(plant, product);
        List<Demand> demandList = new ArrayList<>();
        for(Map map : mapList) {
            Demand demand = new Demand();
            demand.setProduct(product);
            demand.setMaterial((String)map.get("material"));
            //BOM用量
            demand.setUsageQty((double) map.get("usageQty"));
            //替代料比例
            demand.setSubstituteRate((Double) map.get("substituteRate"));
            //切片数
            demand.setSlice((Double) map.get("slice"));
            demandList.add(demand);
        }
        return demandList;
    }

    /**
     * 展开CELL的BOM
     * @param plant
     * @param outputName
     * @return
     */
    public List<Demand> cellDemand(String plant, String outputName) {
        //截取" "之前字符串
        String product = outputName.substring(0, outputName.indexOf(" "));
        if(StringUtils.contains(outputName, ",")) {
            outputName = outputName.substring(0, outputName.indexOf(","));
        }
        List<String> products = new ArrayList<>();
        products.add(outputName);
        //CELL成品料号展开材料
        List<Bom> bomList =  bomService.getProductBom("CELL", outputName);
        List<Bom> bomEffectList = new ArrayList<>();
        for(Bom bom : bomList) {
            if(bom.isEffectFlag() && StringUtils.isNotEmpty(bom.getMaterial_())) {
                bomEffectList.add(bom);
            }
        }

        List<Demand> demandList = new ArrayList<>();
        if(bomEffectList == null || bomEffectList.size()==0) return demandList;
        for(Bom bom : bomEffectList) {
            Demand demand = new Demand();
            demand.setProduct(outputName);
            String material_ = bom.getMaterial_();
            demand.setMaterial(material_);
            //BOM用量
            demand.setUsageQty(bom.getUsageQty());
            //替代料比例
            demand.setSubstituteRate(materialSubstituteService.getMaterialSubstituteRate(plant, product, material_));
            //切片数
            demand.setSlice(productSliceService.getProductSliceService(product));
            demandList.add(demand);
        }
        return demandList;
    }

    /**
     * 深度复制list对象,先序列化对象，再反序列化对象
     * @param src 需要复制的对象列表
     * @return 返回新的对象列表
     * @throws IOException 读取Object流信息失败
     * @throws ClassNotFoundException 泛型类不存在
     */
    private static <T> List<T> deepCopy(List<T> src)
            throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        return (List<T>)in.readObject();
    }


    /**
     * 4.保存MRP材料数据
     * @param mrpVer MrpVer
     */
    public void saveMrpDataMaster(MrpVer mrpVer) {
        log.info("保存MRP材料数据>> START");
        List<Map> materialList = demandService.sumMaterial(Arrays.asList(mrpVer.splitDpsVer()));
        List<MrpDataMaster> mrpDataMasterList = new ArrayList<>();
        int i = materialList.size();
        for(Map map : materialList) {
            String material = (String)map.get("material");
            String product = (String)map.get("product");
            log.info("料号"+material+",剩余"+ (i--) );
            MrpDataMaster mrpDataMaster = new MrpDataMaster();
            mrpDataMaster.setMrpVer(mrpVer.getMrpVer());
            mrpDataMaster.setPlant(mrpVer.getPlant());
            mrpDataMaster.setMaterial(material);
            mrpDataMaster.setMaterialName(materialService.getMaterialName(material));
            mrpDataMaster.setMaterialGroup(materialService.getMaterialGroup(material));
            //设置机种
            mrpDataMaster.setProducts(product);
            //设置供应商
            List<Supplier> materialSupplierList = materialSupplierService.getMaterialSupplier(material);
            String suppler = "";
            for(Supplier supplier : materialSupplierList) {
                if(StringUtils.isNotEmpty(suppler)) {
                    suppler += ",";
                }
                suppler += supplier.getSName();
            }
            mrpDataMaster.setSupplier(suppler);
            //计算损耗率、期初库存
            computeMrpDataMaster(mrpVer, mrpDataMaster);
            mrpDataMasterList.add(mrpDataMaster);
        }
        log.info("保存MrpDataMaster...");
        long time = System.currentTimeMillis();
        batchService.batchInsert(mrpDataMasterList);
        log.info("保存耗时："+ (System.currentTimeMillis()-time));
        log.info("保存MRP材料数据>> END");
    }

    /**
     * 4.1计算损耗率、期初库存
     * @param mrpDataMaster MrpDataMaster
     */
    public void computeMrpDataMaster(MrpVer mrpVer, MrpDataMaster mrpDataMaster) {
        //损耗率
        double lossRate = materialLossRateService.getMaterialLossRate(mrpDataMaster.getMaterial());
        mrpDataMaster.setLossRate(lossRate);
        //期初库存
        double goodInventory = inventoryService.getGoodInventory(mrpDataMaster.getPlant(), mrpDataMaster.getMaterial(), mrpVer.getStartDate());
        double dullInventory = inventoryService.getDullInventory(mrpDataMaster.getPlant(), mrpDataMaster.getMaterial(), mrpVer.getStartDate());
        mrpDataMaster.setGoodInventory(goodInventory);
        mrpDataMaster.setDullInventory(dullInventory);
    }

    /**
     * 5.计算MRP
     * @param mrpVer MrpVer
     */
    public void computeMrp(MrpVer mrpVer) throws IOException, ClassNotFoundException {
        log.info("计算MRP>> START");
        List<MrpDataMaster> mrpDataMasterList = mrpDataMasterRepository.findByMrpVer(mrpVer.getMrpVer());
        Date startDate = mrpVer.getStartDate();
        Date endDate = mrpVer.getEndDate();
        List<java.util.Date> dateList = DateUtil.getCalendar(startDate, endDate);
        List<MrpData> modeList = new ArrayList<>();
        for(java.util.Date date : dateList) {
            MrpData mrpData = new MrpData();
            mrpData.setFabDate(new Date(date.getTime()));
            mrpData.setMrpVer(mrpVer.getMrpVer());
            modeList.add(mrpData);
        }
        int i = mrpDataMasterList.size();
        List<MrpData> mrpDataList = new ArrayList<>();
        for(MrpDataMaster mrpDataMaster : mrpDataMasterList) {
            log.info("计算料号"+mrpDataMaster.getMaterial()+"剩余"+ (i--) );
            mrpDataList.addAll(
                    computeMrp(mrpVer, mrpDataMaster, deepCopy(modeList))
            );
        }
        log.info("保存MrpData...");
        long time = System.currentTimeMillis();
        batchService.batchInsert(mrpDataList);
        log.info("保存耗时："+ (System.currentTimeMillis()-time));
        log.info("计算MRP>> END");
    }

    /**
     * 5.1计算料号的MRP
     * @param mrpDataMaster MrpDataMaster
     * @param mrpDataMaster MrpDataMaster
     * @param mrpDataList 模板
     */
    public List<MrpData> computeMrp(MrpVer mrpVer, MrpDataMaster mrpDataMaster, List<MrpData> mrpDataList) {
        List<Map> demandList = demandService.sumMaterialDemand(Arrays.asList(mrpVer.splitDpsVer()), mrpDataMaster.getMaterial());
        HashMap<Date, Double> demandMap = new HashMap<>();
        for(Map map : demandList) {
            demandMap.put((Date) map.get("fabDate"), ((BigDecimal) map.get("demandQty")).doubleValue());
        }
        //损耗率
        double lossRate = mrpDataMaster.getLossRate();
        //期初库存
        double inventory = mrpDataMaster.getGoodInventory();

        //缺料分配
        List<MrpAllocation> allocationList = new ArrayList<>();
        for(int i=0; i<mrpDataList.size(); i++) {
            MrpData mrpData = mrpDataList.get(i);
            mrpData.setMaterial(mrpDataMaster.getMaterial());
            //需求量
            Double demandQty = demandMap.get(mrpData.getFabDate());
            if( demandQty == null) {
                demandQty = 0D;
            }
            mrpData.setDemandQty(demandQty);

            // 损耗量
            double lossQty = DoubleUtil.multiply(demandQty, lossRate);

            // 到货量
            double arrivalQty = supplierArrivalPlanService.getMaterialArrivalPlanQty(mrpVer.getPlant(),
                    mrpData.getMaterial(), mrpData.getFabDate());
            //ARY/CELL/CELL向上取整
            lossQty = DoubleUtil.upPrecision(lossQty, 0);
            mrpData.setLossQty(lossQty);
            mrpData.setArrivalQty(arrivalQty);


            // 结余量
            double balanceQty;
            if(i==0) {
                // 第一天：期初库存 – 当日需求量 - 当日耗损量
                balanceQty = DoubleUtil.sum(inventory, -demandQty, -lossQty);
            } else {
                MrpData yesterdayMrpData = mrpDataList.get(i-1);
                double yesterdayBalanceQty = yesterdayMrpData.getBalanceQty();
                double yesterdayArrivalQty = yesterdayMrpData.getArrivalQty();
                // 其他天：前一天计算的可用库存 + 前一天的到货 - 当日耗损量 – 当日需求量
                balanceQty = DoubleUtil.sum(yesterdayBalanceQty, yesterdayArrivalQty, -demandQty, -lossQty);
            }
            //ARY/CELL/CELL向上取整
            balanceQty = DoubleUtil.upPrecision(balanceQty, 0);

            //判断结余量是否修改
            if(mrpData.isModifyBalanceFlag()) {
                mrpData.setBalanceQtyHis(balanceQty);
                balanceQty = mrpData.getBalanceQty();
            }
            mrpData.setBalanceQty(balanceQty);

            //缺料量
            double shortQty = 0;
            if(i==0) {
                if(balanceQty<0) {
                    shortQty = balanceQty;
                }
            } else {
                if(balanceQty<0) {
                    MrpData yesterdayMrpData = mrpDataList.get(i-1);
                    double yesterdayBalanceQty = yesterdayMrpData.getBalanceQty();
                    if(yesterdayBalanceQty>0) {
                        shortQty = -demandQty;
                    } else {
                        shortQty = DoubleUtil.subtract(balanceQty,yesterdayBalanceQty);
                    }
                }
            }
            //ARY/CELL/CELL向上取整
            shortQty = DoubleUtil.upPrecision(shortQty, 0);
            mrpData.setShortQty(shortQty);

            //系统进行缺料分配，只分配独家供应商的料号
            double allocationQty = mrpAllocationService.getAllocationQry(mrpVer.getPlant(), mrpData.getMaterial(), mrpData.getFabDate());
//            if(shortQty < 0 && allocationQty == 0) {
//                List<Supplier> supplierList = materialSupplierService.getMaterialSupplier(mrpData.getMaterial());
//                if(supplierList.size() == 1) {
//                    allocationQty = -shortQty;
//                    MrpAllocation mrpAllocation = new MrpAllocation();
//                    mrpAllocation.setPlant(mrpVer.getPlant());
//                    mrpAllocation.setMaterial(mrpData.getMaterial());
//                    mrpAllocation.setFabDate(mrpData.getFabDate());
//                    mrpAllocation.setSupplier(supplierList.get(0).getId());
//                    mrpAllocation.setAllocationQty(allocationQty);
//                    mrpAllocation.setSupplierName(supplierList.get(0).getSName());
//                    mrpAllocation.setMaterialName(mrpDataMaster.getMaterialName());
//                    mrpAllocation.setMaterialGroup(mrpDataMaster.getMaterialGroup());
//                    mrpAllocation.setMemo("系统分配");
//                    mrpAllocation.setCreateDate(new java.util.Date());
//
//                    allocationList.add(mrpAllocation);
//                }
//            }
            mrpData.setAllocationQty(allocationQty);
        }
        mrpAllocationService.saveMrpAllocation(allocationList);
        return mrpDataList;
    }

    public String runMrp(String[] dpsVers, String[] mpsVers) throws IOException, ClassNotFoundException {
        checkVer(dpsVers, null);
        MrpVer mrpVer = createMrpVer(dpsVers, null);
        computeDemand(mrpVer);
        saveMrpDataMaster(mrpVer);
        computeMrp(mrpVer);
        return mrpVer.getMrpVer();
    }

    //1. DPS&MPS版本检查
    //2. 创建MRP版本
    //3. 计算材料的需求
    //3.1 计算DPS的材料的需求
    //3.2 计算机种的需求
    //3.3 展开BOM
    //3.4 替代料处理
    //3.5 CELL切片数
    //4 TP结算
    //5.运行MRP计算

    /**
     * 更新计算MRP的某个材料
     * @param mrpVer MRP版本
     * @param mrpDataMaster MrpDataMaster
     */
    public void updateMrp(MrpVer mrpVer, MrpDataMaster mrpDataMaster) {
        //更新损耗率、期初库存
        computeMrpDataMaster(mrpVer, mrpDataMaster);
        List<MrpData> mrpDataList = mrpDataService.getMrpData(mrpVer.getMrpVer(), mrpDataMaster.getMaterial());
        //更新MRP计算
        computeMrp(mrpVer, mrpDataMaster, mrpDataList);
        mrpDataMasterService.saveMrpMaterial(mrpDataMaster);
        mrpDataRepository.saveAll(mrpDataList);
    }

    /**
     * 更新MRP
     * @param mrpVer MRP版本
     */
    public void updateMrp(MrpVer mrpVer) {
        log.info("更新MRP>> START");
        List<MrpDataMaster> mrpDataMasterList = mrpDataMasterService.getMrpMaterial(mrpVer.getMrpVer());
        int l = mrpDataMasterList.size();
        for(MrpDataMaster mrpDataMaster : mrpDataMasterList) {
            log.info("更新料号"+mrpDataMaster.getMaterial()+"剩余"+ (l--) );
            updateMrp(mrpVer, mrpDataMaster);
        }
        log.info("更新MRP>> END");
    }
}
