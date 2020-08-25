package com.ivo.mrp2.entity;

import com.ivo.mrp2.key.MrpAllocationPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * MRP的分配情况
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_allocation")
@IdClass(MrpAllocationPrimaryKey.class)
public class MrpAllocation  {

    /**
     * 厂别
     */
    @Id
    private String plant;

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
    private String supplier;

    private java.sql.Date adate;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 分配数量
     */
    private double allocationQty;

    /**
     * 发布给厂商标志
     */
    private boolean releaseFlag=false;

    /**
     * 发布给厂商日期
     */
    private Date releaseDate;

    /**
     * 厂商答复日期
     */
    private java.sql.Date replyDate;

    /**
     * 厂商答复数量
     */
    private Double replyQty;

    /**
     * 备注
     */
    private String memo;

    /**
     * 创建日期
     */
    private Date createDate = new Date();
}
