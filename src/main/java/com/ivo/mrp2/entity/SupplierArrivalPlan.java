package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import com.ivo.mrp2.key.SupplierArrivalPlanKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Date;

/**
 * 供应商到货计划
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp_supplier_arrival_plan")
@IdClass(SupplierArrivalPlanKey.class)
public class SupplierArrivalPlan {

    /**
     * 供应商编号
     */
    @Id
    private String supplierCode;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Id
    private Date date;

    /**
     * 料号
     */
    @Id
    private String material;

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

    /**
     * 厂别
     */
    @Id
    private String plant;

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
    private String supplier;

    /**
     * 计划到货量
     */
    private Double arrivalQty;
}
