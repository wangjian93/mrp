package com.ivo.mrp.entity.direct.cell;

import com.ivo.mrp.key.DpsKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * CELL的DPS
 * 抓取PC的DPS中的CELL input
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Cell")
@IdClass(DpsKey.class)
public class DpsCell {

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
     * DPS的outputName，多个时用逗号分割
     */
    private String outputName;

    /**
     * 机种
     */
    private String project;

    /**
     * DPS的outputName，对应MPS中的命名
     */
    @Id
    private String product;

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

    private boolean splitFlag = false;

}
