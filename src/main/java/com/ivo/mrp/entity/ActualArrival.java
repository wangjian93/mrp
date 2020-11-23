package com.ivo.mrp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 供应商的实际到货
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Actual_Arrival")
public class ActualArrival extends AutoIncreaseEntityModel {

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 料号
     */
    private String material;

    private String materialName;

    private String materialGroup;

    /**
     * 收货数量
     */
    private double qty;

    /**
     * 收货单号
     */
    private String orderNumber;

    /**
     * 供应商ID
     */
    private String supplierCode;

    private String supplierName;

    /**
     * 工厂
     */
    private String werks;

    /**
     * 仓位
     */
    private String lgort;

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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate = new Date();
}
