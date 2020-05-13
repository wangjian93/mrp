package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Date;

/**
 * 供应商到货计划
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp2_supplier_arrival_plan")
public class SupplierArrivalPlan extends AutoIncreaseEntityModel {

    /**
     * 供应商编号
     */
    private long supplierCode;

    /**
     * 供应商名称
     */
    private String supplier;

    /**
     * 料号
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 计划到货量
     */
    private Double planQty;
}
