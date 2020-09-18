package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
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
public class MrpPackage extends AutoIncreaseEntityModel {

    public static final String TYPE_D = "单片";
    public static final String TYPE_L = "连片";

    /**
     * MRP版本
     */
    private String ver;

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
     * 料号
     */
    private String material;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 需求量
     */
    private double demandQty;

    /**
     * 计划到货量
     */
    private double arrivalQty;

    /**
     * 分配数量
     */
    private double allocationQty;

    /**
     * 缺料量
     */
    private double shortQty;

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
