package com.ivo.mrp.entity.pol;

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
@Table(name = "MRP3_Dps_Cell_Pol_Product")
public class DpsCellPolProduct extends AutoIncreaseEntityModel {

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
    private String project;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 需求量
     */
    private double demandQty;
}
