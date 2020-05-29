package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpData;
import com.ivo.mrp2.entity.MrpVer;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;

/**
 * MRP计算服务接口
 * @author wj
 * @version 1.0
 */
public interface MrpService2 {

    /**
     * 获取MRP的所有版本字符串
     * @return List<String>
     */
    List<String> getMrpVerStr();

    /**
     * 获取MRP的所有版本
     * @return List<String>
     */
    List<MrpVer> getMrpVer();

    /**
     * 分页获取MRP版本信息
     * @param page 页数
     * @param limit 分页大小
     * @return Page
     */
    Page<MrpVer> getMrpVer(int page, int limit);

    /**
     * 获取MrpVer对象
     * @param mrpVer mrp版本
     * @return MrpVer
     */
    MrpVer getMrpVer(String mrpVer);





    /**
     * 获取MRP版本的数据
     * @param mrpVer MRP版本
     * @param material 料号
     * @return List<MrpData>
     */
    List<MrpData> getMrpData(String mrpVer, String material);

    /**
     * 获取MRP版本的数据
     * @param mrpVer MRP版本
     * @param materials 料号集合
     * @return List<MrpData>
     */
    List<MrpData> getMrpData(String mrpVer, List<String> materials);

    /**
     * 获取MRP版本的数据
     * @param mrpVer MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return MrpData
     */
    MrpData getMrpData(String mrpVer, String material, Date fabDate);




    /**
     * 获取MRP版本的日历
     * @param mrpVer mrp版本
     * @return List<Date>
     */
    List<Date> getMrpCalendarList(String mrpVer);




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
     * DPS展开BOM计算材料需求量
     * @param mrpVer MRP版本
     */
    void computeDemand(String mrpVer);

    /**
     * DPS展开BOM计算材料需求量
     * @param dpsVer DPS版本
     * @param product 机种
     */
    void computeDemand(String dpsVer, String product);




    /**
     * 生成MRP版本数据
     * @param mrpVer MRP版本
     */
    void generateMrpData(String mrpVer);

    /**
     * 生成MRP版本数据
     * @param mrpVer MRP版本
     * @param material 机种
     */
    void generateMrpData(String mrpVer, String material);





    /**
     * 计算损耗量
     * @param mrpVer MRP版本
     */
    void computeLossQty(String mrpVer);

    /**
     * 计算损耗量
     * @param mrpVer MRP版本
     * @Param material 料号
     */
    void computeLossQty(String mrpVer, String material);




    /**
     * 计算损耗量
     * @param mrpVer MRP版本
     */
    void computeArrivalQty(String mrpVer);

    /**
     * 计算损耗量
     * @param mrpVer MRP版本
     * @Param material 料号
     */
    void computeArrivalQty(String mrpVer, String material);




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
     * 更新计算材料的MRP数据
     * @param mrpVer MRP版本
     * @param material 料号
     */
    void updateMrpData(String mrpVer, String material, Date fabDate);


    /**
     * 修改结余量
     * @param  mrpData mrpData
     */
    void updateBalanceQty(MrpData mrpData);
}
