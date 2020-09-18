package com.ivo.mrp.entity.direct;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 厂商答复
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Supplier_Reply")
public class AllocationReply extends AutoIncreaseEntityModel {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 料号
     */
    private String material;

    /**
     * 供应商ID
     */
    private String supplierCode;

    /**
     * 日期
     */
    private java.sql.Date fabDate;

    /**
     * 答复日期
     */
    private double replyDate;

    /**
     * 答复数量
     */
    private double replyQty;

    /**
     * 有效性
     */
    private boolean validFlag;

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createDate = new Date();

    /**
     * 修改者
     */
    private  String updater;

    /**
     * 修改时间
     */
    private Date updateDate;
}
