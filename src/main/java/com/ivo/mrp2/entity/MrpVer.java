package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * MRP运算版本
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity(name = "mrp_mrp_ver")
public class MrpVer implements Serializable {

    public static final String STATUS_COMP = "COMP";
    public static final String STATUS_INPR = "INPR";
    public static final String STATUS_WAIT = "WAIT";

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

    /**
     * 周期开始
     */
    private Date startDate;

    /**
     * 周期结束
     */
    private Date endDate;

    /**
     * 选择的DPS版本
     */
    private String dpsVer;

    /**
     * 选择的Mps版本
     */
    private String mpsVer;

    /**
     * 厂别
     */
    private String plant;

    /**
     * 运算状态
     */
    private String status;
}
