package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.mrp2.key.MaterialSubstituteKey;
import com.ivo.mrp2.key.MonthSettlementKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@Table(name = "mrp2_month_settlement")
@IdClass(MonthSettlementKey.class)
public class MonthSettlement {

    /**
     * 厂别
     */
    @Id
    private String plant;

    /**
     * 月份
     */
    @Id
    private String month;

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * 物料组
     */
    @Id
    private String materialGroup;

    /**
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 数量
     */
    private double qty;

    /**
     * 放入下一个月的某一天
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date settlementDate;
}
