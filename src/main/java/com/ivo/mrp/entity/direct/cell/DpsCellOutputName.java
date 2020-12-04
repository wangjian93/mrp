package com.ivo.mrp.entity.direct.cell;

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
@Table(name = "MRP3_Dps_Cell_OutputName")
public class DpsCellOutputName extends AutoIncreaseEntityModel {

    /**
     * 版本
     */
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * DPS的outputName，多个时用逗号分割
     */
    private String outputName;

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
