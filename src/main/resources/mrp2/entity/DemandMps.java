package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
//@Entity
//@Table(name = "mrp2_Demand_Mps")
@Setter
@Getter
public class DemandMps {

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

    /**
     * 生产日期
     */
    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.sql.Date fabDate;

    /**
     * 料号
     */
    @Id
    private String material;

    /**
     * 需求量
     */
    private double demandQty;

    /**
     * MPS版本
     */
    @Id
    private String mpsVer;

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * MPS的机种需求量
     */
    private double mpsQty;

    /**
     * 机种材料的BOM使用量
     */
    private double usageQty;

    /**
     * CELL/ARY机种的切片数
     */
    private Double slice;

    /**
     * 替代比例
     */
    private Double substituteRate;

    /**
     * 来源类型
     */
    private String sourceType;

    /**
     * 创建时间
     */
    private Date createDate;
}
