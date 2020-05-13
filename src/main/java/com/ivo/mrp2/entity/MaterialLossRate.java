package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Date;

/**
 * MRP使用的物料损耗率 （数据MC物管自维护）
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp2_material_loss_rate")
public class MaterialLossRate extends AutoIncreaseEntityModel {

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 物料
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 损耗率
     */
    private Double lossRate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectDate;

    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 说明
     */
    private String memo;
}
