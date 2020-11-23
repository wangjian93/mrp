package com.ivo.mrp.entity.direct.cell;

import com.ivo.mrp.key.MrpKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * CELL的MRP
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Cell")
@IdClass(MrpKey.class)
public class MrpCell {

    /**
     * MRP版本
     */
    @Id
    private String ver;

    /**
     * 料号
     */
    @Id
    private String material;

    /**
     * 日期
     */
    @Id
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
    private boolean modifyBalanceFlag;

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
    private String creator = "";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
