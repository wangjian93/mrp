package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpData;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpDataService {

    /**
     * 获取MRP版本的数据
     * @param mrpVer 版本
     * @return List<MrpData>
     */
    List<MrpData> getMrpData(String mrpVer);

    /**
     * 获取MRP版本的缺料数据
     * @param mrpVer MRP版本
     * @return List<MrpData>
     */
    List<MrpData> getShortMrpData(String mrpVer, List<String> material);

    /**
     * 获取料号的MRP数据
     * @param mrpVer MRP版本
     * @param materialList 料号
     * @return List<MrpData>
     */
    List<MrpData> getMrpData(String mrpVer, List<String> materialList);

    List<MrpData> getMrpData(String mrpVer, List<String> materialList, Date startDate, Date endDate);

    /**
     * 获取单个料号的MRP数据
     * @param mrpVer  MRP版本
     * @param material 料号
     * @return List<MrpData>
     */
    List<MrpData> getMrpData(String mrpVer, String material);

    /**
     * 获取材料需求的详细
     * @param mrpVer MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return List<Map>
     */
    List<Map> getMaterialDemandDetail(String mrpVer, String material, Date fabDate);

    /**
     * 修改结余量
     * @param mrpVer MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @param balanceQty 结余量
     * @param memo 备注
     */
    void editBalance(String mrpVer, String material, Date fabDate, double balanceQty, String memo);

    /**
     * 获取MrpData
     * @param mrpVer MRP版本
     * @param material 料号
     * @param fabDate 日期
     * @return
     */
    MrpData getMrpData(String mrpVer, String material, Date fabDate);

    void save(MrpData mrpData);

    void save(List<MrpData> mrpDataList);

    Page<String> getPageMaterialForShort(int page, int limit, List<String> verList, String material);
}
