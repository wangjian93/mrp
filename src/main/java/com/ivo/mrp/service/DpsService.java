package com.ivo.mrp.service;

import com.ivo.mrp.entity.*;
import com.ivo.mrp.entity.direct.ary.DpsAry;
import com.ivo.mrp.entity.direct.ary.DpsAryOc;
import com.ivo.mrp.entity.direct.cell.DpsCell;
import com.ivo.mrp.entity.direct.lcm.DpsLcm;
import com.ivo.mrp.entity.packaging.DpsPackage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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
     * 获取CELL包材DPS
     * @param ver dps版本
     * @return List<DpsPackage>
     */
    List<DpsPackage> getDpsPackage(String ver);

    /**
     * 获取ARY OC DPS
     * @param ver dps版本
     * @return List<DpsAryOc>
     */
    List<DpsAryOc> getDpsAryOc(String ver);

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
     * 同步包材DPS
     */
    void syncDpsPackage();
    void syncDpsPackage(String ver);

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
     * 获取LCM DPS的所有机种
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
     * 获取包材 DPS的所有机种
     * @param ver DPS版本
     * @return List<Map>
     */
    List<Map> getDpsPackageProduct(String ver);

    /**
     * 获取包材 机种的DPS
     * @param ver DPS版本
     * @param product 机种
     * @param type 单片/连片类型
     * @param linkQty 连片数
     * @param mode 切单模式
     * @return List<DpsPackage>
     */
    List<DpsPackage> getDpsPackage(String ver, String product, String type, Double linkQty, String mode);

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
}
