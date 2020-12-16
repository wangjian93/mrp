package com.ivo.mrp.entity.packaging;

import com.ivo.mrp.key.DpsPackageKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * cell 包材DPS
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Package")
@IdClass(DpsPackageKey.class)
public class DpsPackage {

    @Id
    private String ver;

    @Id
    private String packageId;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 单片/连片
     */
    private String type;

    /**
     * 连片数
     */
    private String linkQty;

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
