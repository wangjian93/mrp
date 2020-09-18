package com.ivo.mrp.entity.direct.lcm;

import com.ivo.mrp.key.DpsKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * LCM DPS数据
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Lcm")
@IdClass(DpsKey.class)
public class DpsLcm {

    @Id
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种 (成品料号)
     */
    @Id
    private String product;

    /**
     * 机种
     */
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
