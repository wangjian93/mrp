package com.ivo.mrp.service;

import com.ivo.mrp.entity.*;
import com.ivo.mrp.entity.direct.ary.DpsAry;
import com.ivo.mrp.entity.direct.ary.DpsAryOc;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;

/**
 * DPS数据服务接口
 * @author wj
 * @version 1.0
 */
public interface DpsService {

    /**
     * 获取DPS版本对象
     * @param ver dps版本
     * @return DpsVer
     */
    DpsVer getDpsVer(String ver);

    /**
     * 生成DPS版本
     * @return String
     */
    String generateDpsVer();

    /**
     * 同步LCM DPS
     */
    void syncDpsLcm();
    void syncDpsLcm(String ver);

    /**
     * 同步CELL DPS
     */
    void syncDpsCell();
    void syncDpsCell(String ver);

    /**
     * 同步Ary DPS
     */
    void syncDpsAry();
    void syncDpsAry(String ver);
    /**
     * 同步Ary的Oc DPS
     */
    void syncDpsAryOc(String dpsVer, String fileVer);



    /**
     * 获取ARY DPS
     * @param ver dps版本
     * @return List<DpsAry>
     */
    List<DpsAry> getDpsAry(String ver);

    /**
     * 获取CELL DPS
     * @param ver dps版本
     * @return  List<DpsCell>
     */
    List<DpsCell> getDpsCell(String ver);

    /**
     * 获取LCM DPS
     * @param ver dps版本
     * @return List<DpsLcm>
     */
    List<DpsLcm> getDpsLcm(String ver);



    /**
     * 获取ARY OC DPS
     * @param ver dps版本
     * @return List<DpsAryOc>
     */
    List<DpsAryOc> getDpsAryOc(String ver);

    /**
     * 获取Ary DPS的所有机种
     * @param ver DPS版本
     * @return List<String>
     */
    List<String> getDpsAryProduct(String ver);

    /**
     * 获取Ary OC DPS的所有机种
     * @param ver DPS版本
     * @return List<String>
     */
    List<String> getDpsAryOcProduct(String ver);

    /**
     * 获取CELL DPS的所有机种
     * @param ver DPS版本
     * @return List<String>
     */
    List<String> getDpsCellProduct(String ver);

    /**
     * 获取LCM DPS的机种
     * @param ver DPS版本
     * @return List<String>
     */
    List<String> getDpsLcmProduct(String ver);

    /**
     * 获取ARY 机种的DPS
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsAry>
     */
    List<DpsAry> getDpsAry(String ver, String product);

    /**
     * 获取ARY 机种OC的DPS
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsAry>
     */
    List<DpsAryOc> getDpsAryOc(String ver, String product);

    /**
     * 获取CELL 机种的DPS
     * @param ver dps版本
     * @param product 机种
     * @return  List<DpsCell>
     */
    List<DpsCell> getDpsCell(String ver,  String product);

    /**
     * 获取LCM 机种的DPS
     * @param ver dps版本
     * @param product 机种
     * @return List<DpsLcm>
     */
    List<DpsLcm> getDpsLcm(String ver,  String product);





    /**
     * 分页查询DPS版本信息
     * @param page 页数
     * @param limit 分页大小
     * @param searchFab 查询厂别
     * @param searchType 查询类型
     * @param searchVer 查询版本
     * @return Page<DpsVer>
     */
    Page<DpsVer> queryDpsVer(int page, int limit, String searchFab, String searchType, String searchVer);

    /**
     * 获取DPS版本的数据
     * @param ver dps版本
     * @return List
     */
    List getDpsDate(String ver);

    /**
     * 获取DPS的日历
     * @param ver MRP版本
     * @return List<Date>
     */
    List<Date> getDpsCalendar(String ver);

    /**
     * 获取机种DPS数据
     * @param ver 版本
     * @param searchProduct 查询机种条件
     * @return Page
     */
    List getProductDpsData(String ver, String searchProduct);

    /**
     * 分页获取DPS中的机种
     * @param ver DPS版本
     * @param searchProduct 机种
     * @return Page
     */
    Page getPageProduct(String ver, int page, int limit, String searchProduct);

    /**
     * 查询获取Ary DPS的所有机种
     * @param ver DPS版本
     * @return List<String>
     */
    Page getPagDpsAryProduct(String ver, int page, int limit, String searchProduct);

    /**
     * 查询获取Ary OC DPS的所有机种
     * @param ver DPS版本
     * @return List<String>
     */
    Page getPagDpsAryOcProduct(String ver, int page, int limit, String searchProduct);

    /**
     * 查询获取CELL DPS的所有机种
     * @param ver DPS版本
     * @return List<String>
     */
    Page getPagDpsCellProduct(String ver, int page, int limit, String searchProduct);

    /**
     * 查询获取LCM DPS的机种
     * @param ver DPS版本
     * @return List<String>
     */
    Page getPagDpsLcmProduct(String ver, int page, int limit, String searchProduct);

    /**
     * 获取LCM 机种的DPS
     * @param ver 版本
     * @param product 机种
     * @param startDate 开始日期
     * @return List<DpsLcm>
     */
    List<DpsLcm> getDpsLcm(String ver,  String product, Date startDate);

    /**
     * 获取Ary机种的DPS
     * @param ver 版本
     * @param product 机种
     * @param startDate 开始日期
     * @return List<DpsLcm>
     */
    List<DpsAry> getDpsAry(String ver,  String product, Date startDate);

    /**
     * 获取Ary OC 机种的DPS
     * @param ver
     * @param product
     * @param startDate
     * @return
     */
    List<DpsAryOc> getDpsAryOc(String ver,  String product, Date startDate);

    /**
     * 获取CELL 机种的DPS
     * @param ver 版本
     * @param product 机种
     * @param startDate 开始日期
     * @return List<DpsLcm>
     */
    List<DpsCell> getDpsCell(String ver,  String product, Date startDate);

    /**
     * 导入DPS
     * @param inputStream 输入流
     * @param fileName 文件名
     * @return DPS版本
     */
    String importLcmDps(InputStream inputStream, String fileName);


    List<DpsCell> getDpsCellByOutputName(String ver, String outputName);

    List<DpsAry> getDpsAryByOutputName(String ver, String outputName);

    void deleteDpsCell(List<DpsCell> dpsCellList);

    void saveDpsCell(List<DpsCell> list);

    void deleteDpsAry(List<DpsAry> list);

    void saveDpsAry(List<DpsAry> list);

    List<DpsVer> getDpsVerByFileVer(String ver, String type);

    void saveDpsVer(DpsVer dpsVer);
}
