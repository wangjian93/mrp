package com.ivo.mrp.entity.pol;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * POL的DPS
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "MRP3_Dps_Pol")
@Setter
@Getter
public class DpsPol extends AutoIncreaseEntityModel {

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

    private String memo = "";

    private String creator = "";

    private Date createDate = new Date();

}
