package com.ivo.mrp.entity.direct.lcm;

import com.ivo.mrp.key.MpsKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * LCM的MPS
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mps_Lcm")
@IdClass(MpsKey.class)
public class MpsLcm {

    @Id
    private String ver;

    /**
     * 机种（成品料号）
     */
    @Id
    private String product;

    /**
     * 机种
     */
    private String project;

    /**
     * 多版本的机种例如：X140NVFC-0E1M29/M26/M25，截取第一版的机种
     */
    private String fullName;

    /**
     * 日期
     */
    @Id
    private java.sql.Date fabDate;

    /**
     * 需求量
     */
    private double demandQty;

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 创建者
     */
    private String creator = "";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
