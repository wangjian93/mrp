package com.ivo.mrp2.entity;

import com.ivo.mrp2.key.SupplierArrivalPlanKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 供应商到货计划
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_supplier_arrival_plan")
@IdClass(SupplierArrivalPlanKey.class)
public class SupplierArrivalPlan {

    /**
     * 厂别
     */
    @Id
    private String plant;

    /**
     * 供应商ID
     */
    @Id
    private String supplier;

    /**
     * 料号
     */
    @Id
    private String material;

    /**
     * 日期
     */
    @Id
    private java.sql.Date date;

    /**
     * 计划到货量
     */
    private double arrivalQty;

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
     * 备注
     */
    private String memo;

    /**
     * 创建日期
     */
    private Date createDate = new Date();
}
