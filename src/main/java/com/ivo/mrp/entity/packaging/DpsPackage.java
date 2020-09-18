package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * cell 包材DPS
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Package")
public class DpsPackage extends AutoIncreaseEntityModel {

    public static final String TYPE_D = "单片";
    public static final String TYPE_L = "连片";

    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 单片/连片
     */
    private String type;

    /**
     * 连片数
     */
    private Double linkQty;

    /**
     * 抽单模式
     */
    private String mode;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 需求量
     */
    private double demandQty;

    private String memo = "";

    private String creator = "";

    private Date createDate = new Date();
}
