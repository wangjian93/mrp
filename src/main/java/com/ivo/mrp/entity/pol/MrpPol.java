package com.ivo.mrp.entity.pol;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * POL的MRP
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "MRP3_Mrp_Pol")
@Setter
@Getter
public class MrpPol extends AutoIncreaseEntityModel {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 料号
     */
    private String material;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 投入量
     */
    private double inputQty;

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
