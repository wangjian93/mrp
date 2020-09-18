package com.ivo.mrp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 材料的损耗率
 * 分为按料号维护和按物料组维护两种方式
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Loss_Rate")
public class LossRate extends AutoIncreaseEntityModel {

    //表示维护的是料号损耗率
    public static final int  type_material = 0;
    //表示维护的是物料组损耗率
    public static final int type_materialGroup = 1;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 物料
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 损耗率（百分制）
     */
    private double lossRate;

    /**
     * 类型：
     * 0.维护的是物料
     * 1.维护的是物料组
     */
    private int type;

    /**
     * 有效标志
     */
    private boolean validFlag = true;

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
