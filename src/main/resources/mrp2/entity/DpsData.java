package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.mrp2.key.DpsPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Date;

/**
 * DPS数据
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_dps_data")
@IdClass(DpsPrimaryKey.class)
public class DpsData {

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
     * Model
     */
    private String model;

    /**
     * 生产数量
     */
    private double qty;

    /**
     * 备注
     */
    private String memo;
}