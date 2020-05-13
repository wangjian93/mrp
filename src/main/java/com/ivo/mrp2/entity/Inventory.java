package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Date;

/**
 * 库存 （数据来源库存）
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp_inventory")
public class Inventory extends AutoIncreaseEntityModel {

    /**
     * 料号
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fabDate;

    /**
     * 库存数量
     */
    private Double qty;
}
