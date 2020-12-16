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
@Table(name = "MRP3_Mrp_Package_Lcm")
public class MrpPackageLcm extends AutoIncreaseEntityModel {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 料号
     */
    private String material;

    /**
     * 机种
     */
    private String product;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 需求量
     */
    private double demandQty;

    /**
     * 损耗量
     */
    private double lossQty;

    /**
     * 供应商到货量
     */
    private double arrivalQty;

    /**
     * 实际收货数量
     */
    private double arrivalActualQty;

    /**
     * 计划到货量
     */
    private double arrivalPlanQty;

    /**
     * 结余量
     */
    private double balanceQty;

    /**
     * 缺料量
     */
    private double shortQty;

    /**
     * 分配数量
     */
    private double allocationQty;

    /**
     * 结余量是否修改标识
     */
    private boolean modifyBalanceFlag = false;

    /**
     * 结余量计算历史
     */
    private double balanceQtyHis;

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
