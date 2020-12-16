package com.ivo.mrp.entity.lcmPackaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Package_Lcm_Material")
public class MrpPackageLcmMaterial extends AutoIncreaseEntityModel {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 料号
     */
    private String material;


    /**
     * 是否共用
     */
    private boolean isAlone;

    /**
     * 机种（料号对应多个机种时使用','分隔）
     */
    private String products;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 损耗率
     */
    private double lossRate;

    /**
     * 期初库存 (良品仓)
     */
    private double goodInventory;

    /**
     * 期初库存 (呆滞仓)
     */
    private double dullInventory;

    /**
     * 哪一天作为期初库存
     */
    private java.sql.Date inventorDate;

    /**
     * 供应商（供应商对应多个机种时使用','分隔）
     */
    private String suppliers;
    private String supplierCodes;

    /**
     * 客户 （多个时使用','分隔）
     */
    private String customers;

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
