package com.ivo.mrp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Getter
@Setter
@Entity
public class VenderArrivalPlanDetail extends AutoIncreaseEntityModel {

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    /**
     * 供应商预计到量
     */
    private Double pp;

    /**
     * IVO预计到量（FCST）
     */
    private Double fcst_ivo;

    /**
     * IVE预计到量（FCST）
     */
    private Double fcst_ive;

    /**
     * IVO实际到量
     */
    private Double act_ivo;

    /**
     * IVE实际到量
     */
    private Double act_ive;

    /**
     * 料号，为了方便查询
     */
    private String material;

    @ManyToOne
    @JoinColumn(name = "venderArrivalPlan_fk")
    @JsonIgnore
    private VenderArrivalPlan venderArrivalPlan;
}
