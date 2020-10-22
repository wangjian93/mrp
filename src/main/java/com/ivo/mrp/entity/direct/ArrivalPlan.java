package com.ivo.mrp.entity.direct;

import com.ivo.mrp.key.ArrivalPlanKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 供应商的到货计划
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_ArrivalPlan")
@IdClass(ArrivalPlanKey.class)
public class ArrivalPlan  {

    /**
     * 厂别
     */
    @Id
    private String fab;

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
     * 供应商ID
     */
    @Id
    private String supplierCode;

    /**
     * 计划到货量
     */
    private double arrivalQty;

    /**
     * 供应商简称
     */
    private String supplierSname;

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

    /**
     * 修改者
     */
    private String updater = "SYS";

    /**
     * 修改时间
     */
    private Date updateDate = new Date();
}
