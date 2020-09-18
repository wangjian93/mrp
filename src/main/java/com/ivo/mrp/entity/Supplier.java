package com.ivo.mrp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Supplier")
public class Supplier {

    /**
     * 厂商ID
     */
    @Id
    private String supplierCode;

    /**
     * 名称
     */
    private String supplierName;

    /**
     * 简称
     */
    private String supplierSname;

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

    /**
     * 修改者
     */
    private String updater = "";

    /**
     * 修改时间
     */
    private Date updateDate = new Date();
}
