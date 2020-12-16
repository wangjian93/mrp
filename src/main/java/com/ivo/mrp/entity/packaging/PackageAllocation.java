package com.ivo.mrp.entity.packaging;

import com.ivo.mrp.key.PackageAllocationKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 包材的分配
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Package_Allocation")
@IdClass(PackageAllocationKey.class)
public class PackageAllocation {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    @Id
    private String packageId;

    /**
     * 机种
     */
    private String product;

    /**
     * 单片/连片
     */
    private String type;

    /**
     * 连片数
     */
    private Double linkQty;

    @Id
    private String material;

    @Id
    private String supplierCode;

    @Id
    private java.sql.Date fabDate;

    /**
     * 分配数量 (要望)
     */
    private double allocationQty;

    private String supplierName;


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
