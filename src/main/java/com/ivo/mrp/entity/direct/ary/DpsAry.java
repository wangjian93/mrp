package com.ivo.mrp.entity.direct.ary;

import com.ivo.mrp.key.DpsKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * ARY的DPS
 * 抓取PC的DPS中的Array input
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Ary")
@IdClass(DpsKey.class)
public class DpsAry {
    /**
     * 版本
     */
    @Id
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * 日期
     */
    @Id
    private java.sql.Date fabDate;

    /**
     * DPS的outputName，多个时用逗号分割
     */
    private String outputName;

    /**
     * 机种
     */
    private String project;

    /**
     * 需求量
     */
    private double demandQty;

    private String memo = "";

    private String creator = "";

    private Date createDate = new Date();
}
