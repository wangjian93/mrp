package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wj
 * @version 1.0
 */
//@Entity
//@Table(name = "mrp2_Demand_Settlement")
@Setter
@Getter
public class DemandSettlement {

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

    /**
     * 生产日期
     */
    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.sql.Date fabDate;

    /**
     * 料号
     */
    @Id
    private String material;

    /**
     * 需求量
     */
    private double demandQty;

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * 物料组
     */
    @Id
    private String materialGroup;

    /**
     * 机种月结的月份
     */
    private String settlementMonth;

    /**
     * 机种月结数量
     */
    private Double settlementQty;
}
