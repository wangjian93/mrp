package com.ivo.mrp.entity.direct.lcm;

import com.ivo.mrp.key.DemandKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Demand_Lcm")
@IdClass(DemandKey.class)
public class DemandLcm {

    public static final String TYPE_DPS = "DPS";
    public static final String TYPE_MPS = "MPS";
    public static final String TYPE_Settle = "SETTLE";

    /**
     * MRP版本
     */
    @Id
    @Column(length = 50)
    private String ver;

    /**
     * 类型：MPS/DPS
     */
    @Id
    @Column(length = 10)
    private String type;

    /**
     * DPS/MPS版本
     */
    private String dpsMpsVer;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    @Id
    @Column(length = 50)
    private String product;

    /**
     * 机种
     */
    private String project;

    /**
     * 料号
     */
    @Id
    @Column(length = 50)
    private String material;

    /**
     * 日期
     */
    @Id
    private java.sql.Date fabDate;

    /**
     * DPS量
     */
    private double qty;

    /**
     * 机种BOM使用量
     */
    private double usageQty;

    /**
     * 替代比例 （百分制）
     */
    private Double substituteRate;

    /**
     * 需求量
     */
    private double demandQty;

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
