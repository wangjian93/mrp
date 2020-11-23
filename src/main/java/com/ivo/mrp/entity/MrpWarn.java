package com.ivo.mrp.entity;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * MRP的运行提醒，记录一些机种没有BOM List
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp3_warn")
public class MrpWarn extends AutoIncreaseEntityModel {

    /**
     * MRP版本
     */
    private String ver;

    /**
     * 机种
     */
    private String product;

    private String type;

    private String memo;

    private boolean validFlag = true;
}
