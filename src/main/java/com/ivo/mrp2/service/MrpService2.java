package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.Bom;
import com.ivo.mrp2.entity.MrpVer;

import java.sql.Date;
import java.util.List;

/**
 * MRP计算服务接口
 * @author wj
 * @version 1.0
 */
public interface MrpService2 {

    /**
     * 获取MrpVer对象
     * @param mrpVer mrp版本
     * @return MrpVer
     */
    MrpVer getMrpVer(String mrpVer);

    /**
     * 选择DPS的版本，生成MRP版本，MRP计算的日期区间由DPS决定
     * @param dpsVer DPS版本
     * @return MRP版本
     */
    String generateMrpVer(String dpsVer);

    /**
     * 选择DPS的版本、日期区间生成MRP版本
     * @param dpsVer DPS版本
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return MRP版本
     */
    String generateMrpVer(String dpsVer, Date startDate, Date endDate);




    /**
     * 展开BOM计算MRP版本的材料需求
     * @param mrpVer MRP版本
     */
    void computeDemand(String mrpVer);

    /**
     * 获取DPS版本中的所有机种
     * @param dpsVer DPS版本
     * @return List<String>
     */
    List<String> getProducts(String dpsVer);

    /**
     * 展开BOM生成机种的材料需求
     * @param dpsVer DPS版本
     * @param product 机种
     */
    void generateDemand(String dpsVer, String product);

    /**
     * 根据产品、厂别，从BOM中获取材料
     * @param product 机种
     * @param plant 厂别
     * @return List<Bom>
     */
    List<Bom> getBomMaterial(String product, String plant);



    /**
     * 生成MRP版本数据
     * @param mrpVer MRP版本
     */
    void generateMrpData(String mrpVer);

    /**
     * 生成机种的MRP版本数据
     * @param mrpVer MRP版本
     * @param product 机种
     */
    void generateMrpData(String mrpVer, String product);


    /**
     * 计算MRP版本的材料结余量
     * @param mrpVer mrp版本
     */
    void computeBalance(String mrpVer);

    /**
     * 计算材料结余量
     * @param mrpVer MRP版本
     * @Param material 料号
     */
    void computeBalance(String mrpVer, String material);

    /**
     * 获取MRP版本的所有材料
     * @param mrpVer MRP版本
     * @return
     */
    List<String> getMaterial(String mrpVer);

    /**
     * 获取MRP版本的日历
     * @param mrpVer mrp版本
     * @return List<Date>
     */
    List<Date> getMrpCalendarList(String mrpVer);
}
