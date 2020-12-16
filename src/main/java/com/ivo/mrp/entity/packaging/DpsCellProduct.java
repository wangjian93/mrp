package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Cell_Product")
public class DpsCellProduct extends AutoIncreaseEntityModel {

    /**
     * 版本
     */
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
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 需求量
     */
    private double demandQty;
}
