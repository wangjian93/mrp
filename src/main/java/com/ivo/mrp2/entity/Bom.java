package com.ivo.mrp2.entity;

import com.ivo.mrp2.key.BomPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * MRP展料号使用的BOM (数据来源Auto PR)
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp_bom")
@IdClass(BomPrimaryKey.class)
public class Bom {

    /**
     * 厂别
     */
    @Id
    private String plant;

    /**
     * product
     */
    @Id
    private String product;

    /**
     * 料号
     */
    @Id
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
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 计量单位
     */
    private String measureUnit;

    /**
     * 使用数量
     */
    private double usageQty;
}
