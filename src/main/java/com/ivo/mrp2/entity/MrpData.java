package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.mrp2.key.MrpDataPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Date;

/**
 * MRP数据
 * @author wj
 * @version 1.0
 */
@Entity(name = "mrp_data")
@Setter
@Getter
@IdClass(MrpDataPrimaryKey.class)
public class MrpData {

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

    /**
     * 日期
     */
    @Id
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fabDate;

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
     * 损耗量
     */
    private double lossQty;

    /**
     * 计划到货量
     */
    private double arrivalQty;

    /**
     * 结余量
     */
    private double balanceQty;

    /**
     * 修改结余量
     */
    private double balanceQty_;
}
