package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Date;

/**
 * 材料每日结余量 （MRP计算的缺料情况）
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp2_material_daily_balance")
public class MaterialDailyBalance extends AutoIncreaseEntityModel {

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
     * 结余量
     */
    private Double balanceQty;

    public MaterialDailyBalance(String mrpVer, String material, String materialName) {
        this.mrpVer = mrpVer;
        this.material = material;
        this.materialName = materialName;
    }

    public MaterialDailyBalance() {
    }
}
