package com.ivo.mrp.entity.direct;

import com.ivo.mrp.key.AllocationKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 供应商分配数量（厂商要望/回复）
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Allocation")
@IdClass(AllocationKey.class)
public class Allocation {

    /**
     * MRP版本
     */
    private String ver;

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
     * 供应商简称
     */
    private String supplierSname;

    /**
     * 分配数量 (要望)
     */
    private double allocationQty;

    /**
     * 是否发布给供应商
     */
    private boolean releaseFlag = false;

    /**
     * 供应商是否回复
     */
    private boolean replyFlag = false;

    /**
     * 是否满足
     */
    private boolean meetFlag = false;

    /**
     * 厂商回复的总量
     */
    private double replyTotalQty;

    /**
     * 原因说明
     */
    private String reason;

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
