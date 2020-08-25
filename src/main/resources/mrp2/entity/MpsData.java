package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.mrp2.key.MpsDataKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_mps_data")
@IdClass(MpsDataKey.class)
public class MpsData {

    /**
     * MPS版本
     */
    @Id
    private String ver;

    /**
     * 生产日期，每月的需求放在当月的一号
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
     * 生产数量
     */
    private double qty;
}
