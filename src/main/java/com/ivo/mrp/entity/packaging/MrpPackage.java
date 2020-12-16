package com.ivo.mrp.entity.packaging;

import com.ivo.mrp.key.MrpPackageKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * CELL包材MRP
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Package")
@IdClass(MrpPackageKey.class)
public class MrpPackage {

    /**
     * MRP版本
     */
    @Id
    private String ver;

    @Id
    private String packageId;

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
     * 料号
     */
    @Id
    private String material;

    @Id
    private java.sql.Date fabDate;

    /**
     * 需求量
     */
    private double demandQty;

    /**
     * 分配数量
     */
    private double allocationQty;

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
