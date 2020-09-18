package com.ivo.mrp.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 替代料
 * 按机种的物料组维护
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Substitute")
public class Substitute extends AutoIncreaseEntityModel {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 料号
     */
    private String material;

    /**
     * 替代比例 （百分制）
     */
    private double substituteRate;

    /**
     * 物料名
     */
    private String materialName;

    /**
     * 物料组名
     */
    private String materialGroupName;

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
