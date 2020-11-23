package com.ivo.mrp.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 良品、呆滞品仓位
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Position")
public class Position extends AutoIncreaseEntityModel {

    public static final int TYPE_Good = 0;

    public static final int TYPE_Dull = 1;

    public static final String FAB_IVO = "ivo";

    public static final String FAB_IVE = "ive";

    /**
     * 工厂：ivo/ive
     */
    private String fab;

    /**
     * 类型：0.良品、1.呆滞
     */
    private int type;

    /**
     * 仓位
     */
    private String position;
}
