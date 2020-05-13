package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Date;

/**
 * 临时表，数据需要汇总
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp2_material_daily_demand_loss_temp")
public class MaterialDailyDemandTemp extends AutoIncreaseEntityModel {

    /**
     * MRP版本
     */
    private String mrpVer;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fabDate;

    /**
     * 料号
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 需求量
     */
    private Double demandQty;

    /**
     * 工厂
     */
    private String fab;

    /**
     * 产品
     */
    private String product;
}
