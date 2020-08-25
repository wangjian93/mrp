package com.ivo.mrp2.entity;

import com.ivo.mrp2.key.MrpDataPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * MRP数据
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "mrp2_data")
@Setter
@Getter
@IdClass(MrpDataPrimaryKey.class)
public class MrpData implements Serializable {

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

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
     * 需求量
     */
    private double demandQty;

    /**
     * 损耗量
     */
    private double lossQty;

    /**
     * 计划到货量
     */
    private double arrivalQty;

    /**
     * 结余量
     */
    private double balanceQty;

    /**
     * 结余量是否修改标识
     */
    private boolean modifyBalanceFlag;

    /**
     * 结余量计算历史
     */
    private double balanceQtyHis;

    /**
     * 缺料量
     */
    private double shortQty;

    /**
     * 分配数量
     */
    private double allocationQty;

    /**
     * 备注说明
     */
    private String memo;

    /**
     * 创建日期
     */
    private Date createDate = new Date();
}
