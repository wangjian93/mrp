package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

/**
 * DPS版本
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_dps_ver")
public class DpsVer {

    public static final int TYPE_DPS = 1;
    public static final int TYPE_SMALL = 2;


    /**
     * DPS版本
     */
    @Id
    private String ver;

    /**
     * 工厂
     */
    private String fab;

    /**
     * 开始日期
     */
    private Date startDate;

    /**
     * 结束日期
     */
    private Date endDate;

    /**
     * 来源
     */
    private String source;

    /**
     * 类型：1.DSP版本   2.修改版本
     */
    private int type;

    /**
     * 修改的小版本
     */
    private String smallVer;

    /**
     * 机种数量
     */
    private int productQty;

    /**
     * 需求数量
     */
    private int demandQty;


}
