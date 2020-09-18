package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 包材的分配
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Allocation_Package")
public class AllocationPackage extends AutoIncreaseEntityModel {

    /**
     * MRP版本
     */
    private String ver;

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
    private Double linkQty;

    /**
     * 抽单模式
     */
    private String mode;

    /**
     * 分配数量 (要望)
     */
    private double allocationQty;

    /**
     * 是否发布给供应商
     */
    private boolean releaseFlag = false;

    /**
     * 供应商是否回复
     */
    private boolean replyFlag = false;

    /**
     * 是否满足
     */
    private boolean meetFlag = false;

    /**
     * 厂商回复的总量
     */
    private double replyTotalQty;

    /**
     * 原因说明
     */
    private String reason;

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
