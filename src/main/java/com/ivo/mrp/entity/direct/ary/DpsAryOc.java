package com.ivo.mrp.entity.direct.ary;

import com.ivo.mrp.key.DpsKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * DPS ARY 2次input：机种CELL端后会返回ARY，需要在ARY2次投OC（光刻胶）材料
 * ARY OC INPUT捞取逻辑为DPS sub_type = 'Array OC Input'
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Ary_Oc")
@IdClass(DpsKey.class)
public class DpsAryOc {

    /**
     * 版本
     */
    @Id
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    @Id
    private String product;

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
