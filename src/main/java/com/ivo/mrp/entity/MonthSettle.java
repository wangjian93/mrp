package com.ivo.mrp.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

/**
 * 月结
 * TP产品周期15天，出现跨月情况，POL的需求量需放在下一个月
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Month_Settle")
public class MonthSettle extends AutoIncreaseEntityModel {

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 月份
     */
    private String month;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 结算数量
     */
    private double settleQty;

    /**
     * 放入下一个月的哪一天结算
     */
    private Date settleDate;

    /**
     * 有效性标识
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
    private java.util.Date createDate = new java.util.Date();

    /**
     * 修改者
     */
    private String updater = "";

    /**
     * 修改时间
     */
    private java.util.Date updateDate = new java.util.Date();
}
