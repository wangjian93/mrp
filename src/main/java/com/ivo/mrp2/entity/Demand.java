package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import com.ivo.mrp2.key.DemandPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Date;

/**
 * DPS通过BOM展开得到的材料需求量
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp_demand")
@IdClass(DemandPrimaryKey.class)
public class Demand {

    /**
     * DPS版本
     */
    @Id
    private String dpsVer;

    /**
     * 生产日期
     */
    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fabDate;

    /**
     * 产品
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
     * 工厂
     */
    private String fab;

    /**
     * 机种的DPS数量
     */
    private double dpsQty;

    /**
     * 材料BOM的单位使用量
     */
    private double usageQty;

    /**
     * 切片数
     */
    private Double slice;

    /**
     * 需求量
     */
    private double demandQty;
}
