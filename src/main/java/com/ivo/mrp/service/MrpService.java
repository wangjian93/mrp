package com.ivo.mrp.service;

import com.ivo.mrp.entity.MrpVer;
import com.ivo.mrp.entity.direct.ary.MrpAry;
import com.ivo.mrp.entity.direct.ary.MrpAryMaterial;
import com.ivo.mrp.entity.direct.cell.MrpCell;
import com.ivo.mrp.entity.direct.cell.MrpCellMaterial;
import com.ivo.mrp.entity.direct.lcm.MrpLcm;
import com.ivo.mrp.entity.direct.lcm.MrpLcmMaterial;
import com.ivo.mrp.entity.packaging.MrpPackage;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;

/**
 * MRP 服务接口
 * @author wj
 * @version 1.0
 */
public interface MrpService {

    /**
     * 保存
     * @param mrpVer MrpVer
     */
    void saveMrpVer(MrpVer mrpVer);

    /**
     * 统计有多少MRP版本
     * @return Long
     */
    long countMrp();

    /**
     * 获取MRP版本对象
     * @param ver mrp版本
     * @return MrpVer
     */
    MrpVer getMrpVer(String ver);


    /**
     * 版本数组转字符，多个用逗号隔开
     * @param vers 版本数组
     * @return String
     */
    String convertAryToString(String[] vers);

    /**
     * 版本字符转数组，截取逗号
     * @param ver 版本
     * @return String[]
     */
    String[] convertStringToAry(String ver);

    /**
     * 保存LCM
     * @param list MrpLcmMaterial集合
     */
    void saveMrpLcmMaterial(List<MrpLcmMaterial> list);

    /**
     * 保存ARY
     * @param list MrpAryMaterial集合
     */
    void saveMrpAryMaterial(List<MrpAryMaterial> list);

    /**
     * 保存CELL
     * @param list MrpCellMaterial集合
     */
    void saveMrpCellMaterial(List<MrpCellMaterial> list);

    /**
     * 获取LCM的MRP材料
     * @param ver MRP版本
     * @return List<MrpLcmMaterial>
     */
    List<MrpLcmMaterial> getMrpLcmMaterial(String ver);

    /**
     * 获取ARY的MRP材料
     * @param ver MRP版本
     * @return List<MrpAryMaterial>
     */
    List<MrpAryMaterial> getMrpAryMaterial(String ver);

    /**
     * 获取CELL的MRP材料
     * @param ver MRP版本
     * @return List<MrpCellMaterial>
     */
    List<MrpCellMaterial> getMrpCellMaterial(String ver);

    /**
     * 获取LCM MRP中的所有料号
     * @param ver MRP版本
     * @return List<String>
     */
    List<String> getMaterialLcm(String ver);

    /**
     * 获取Ary MRP中的所有料号
     * @param ver MRP版本
     * @return List<String>
     */
    List<String> getMaterialAry(String ver);

    /**
     * 获取Cell MRP中的所有料号
     * @param ver MRP版本
     * @return List<String>
     */
    List<String> getMaterialCell(String ver);

    /**
     * 获取MrpLcmMaterial对象
     * @param ver MRP版本
     * @param material 料号
     * @return MrpLcmMaterial
     */
    MrpLcmMaterial getMrpLcmMaterial(String ver, String material);

    /**
     * 获取MrpAryMaterial对象
     * @param ver MRP版本
     * @param material 料号
     * @return MrpAryMaterial
     */
    MrpAryMaterial getMrpAryMaterial(String ver, String material);

    /**
     * 获取MrpCellMaterial对象
     * @param ver MRP版本
     * @param material 料号
     * @return MrpCellMaterial
     */
    MrpCellMaterial getMrpCellMaterial(String ver, String material);

    /**
     * 获取LCM料号的MRP
     * @param ver MRP版本
     * @param material 料号
     * @return List<MrpLcm>
     */
    List<MrpLcm> getMrpLcm(String ver, String material);

    /**
     * 获取ARY料号的MRP
     * @param ver MRP版本
     * @param material 料号
     * @return List<MrpAry>
     */
    List<MrpAry> getMrpAry(String ver, String material);

    /**
     * 获取CELL料号的MRP
     * @param ver MRP版本
     * @param material 料号
     * @return List<MrpCell>
     */
    List<MrpCell> getMrpCell(String ver, String material);

    /**
     * 获取CELL的MRP
     * @param ver MRP版本
     * @return List<MrpCell>
     */
    List<MrpCell> getMrpCell(String ver);

    /**
     * 获取LCM料号的MRP
     * @param ver MRP版本
     * @return List<MrpLcm>
     */
    List<MrpLcm> getMrpLcm(String ver);

    /**
     * 获取ARY料号的MRP
     * @param ver MRP版本
     * @return List<MrpAry>
     */
    List<MrpAry> getMrpAry(String ver);

    /**
     * 获取包材的MRP
     * @param ver MRP版本
     * @return List<MrpPackage>
     */
    List<MrpPackage> getMrpPackage(String ver);

    /**
     * 获取MRP的日历
     * @param ver MRP版本
     * @return List<Date>
     */
    List<Date> getMrpCalendar(String ver);

    /**
     * 保存LCM
     * @param list List<MrpLcm>
     */
    void saveMrpLcm(List<MrpLcm> list);

    /**
     * 保存ARY
     * @param list List<MrpAry>
     */
    void saveMrpAry(List<MrpAry> list);

    /**
     * 保存CELL
     * @param list List<MrpCell>
     */
    void saveMrpCell(List<MrpCell> list);


    /**
     * 获取包材机种的MRP数据
     * @param ver MRP版本
     * @param product 机种
     * @param type 单片、连片类型
     * @param linkQty 连片数
     * @param mode 切单模式
     * @return List<MrpPackage>
     */
    List<MrpPackage> getMrpPackage(String ver, String product, String type, Double linkQty, String mode);

    /**
     * 保存包材MRP
     * @param list MrpPackage
     */
    void saveMrpPackage(List<MrpPackage> list);

    /**
     * 删除包材MRP
     * @param list MrpPackage
     */
    void deleteMrpPackage(List<MrpPackage> list);

    /**
     * 分页查询MRP版本信息
     * @param page 页数
     * @param limit 分页大小
     * @param searchFab 查询厂别
     * @param searchType 查询类型
     * @param searchVer 查询版本
     * @return Page<MrpVer>
     */
    Page<MrpVer> queryMrpVer(int page, int limit, String searchFab, String searchType, String searchVer);

    /**
     * 获取MRP版本的数据
     * @param ver MRP版本
     * @return List
     */
    List getMrpDate(String ver);
}
