package com.ivo.mrp2.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 材料与供应商的关联
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp_material_supplier")
public class MaterialSupplier extends AutoIncreaseEntityModel {

    /**
     * 料号
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 供应商ID
     */
    private String supplierCode;

    /**
     * 供应商
     */
    private String supplier;
}
