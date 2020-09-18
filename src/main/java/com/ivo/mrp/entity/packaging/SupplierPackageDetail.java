package com.ivo.mrp.entity.packaging;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Supplier_Package_Detail")
public class SupplierPackageDetail extends AutoIncreaseEntityModel {

    public static final String TYPE_Box = "纸箱";
    public static final String TYPE_Tray = "Tray";

    @ManyToOne
    @JoinColumn(name="supplier_Package_fk")
    @JsonIgnore
    private SupplierPackage supplierPackage;


    /**
     * 类型：纸箱  Tray
     */
    private String type;

    /**
     * 供应商编号
     */
    private String supplierCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 分配比例
     */
    private double allocationRate;
}
