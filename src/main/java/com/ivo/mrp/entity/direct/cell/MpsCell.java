package com.ivo.mrp.entity.direct.cell;

import com.ivo.mrp.key.MpsKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mps_Cell")
@IdClass(MpsKey.class)
public class MpsCell {

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

    private String tftMtrl;

    /**
     * 需求量
     */
    private double demandQty;

    private String memo = "";

    private String creator = "";

    private Date createDate = new Date();
}
