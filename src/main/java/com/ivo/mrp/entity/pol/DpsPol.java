package com.ivo.mrp.entity.pol;

import com.ivo.common.model.AutoIncreaseEntityModel;
import com.ivo.mrp.key.DpsPolKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@IdClass(DpsPolKey.class)
public class DpsPol {

    /**
     * 版本
     */
    @Id
    private String ver;

    /**
     * 机种
     */
    @Id
    private String product;

    private String project;

    /**
     * 日期
     */
    @Id
    private java.sql.Date fabDate;

    /**
     * 需求量
     */
    private double demandQty;

    private String memo = "";

    private String creator = "";

    private Date createDate = new Date();

}
