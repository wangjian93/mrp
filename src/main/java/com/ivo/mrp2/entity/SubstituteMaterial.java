package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

/**
 * 替代料关系维护
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp_substitute_material")
public class SubstituteMaterial extends AutoIncreaseEntityModel {

    /**
     * 替代组
     */
    private int substituteGroup;

    /**
     * 厂别
     */
    private String plant;

    /**
     * 机种
     */
    private String product;

    /**
     * 料号
     */
    private String material;

    /**
     * 物料描述
     */
    private String materialName;

    /**
     * 替代比例
     */
    private double substituteRate;

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effectDate;

    /**
     * 失效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

    /**
     * 有效标志
     */
    private boolean effectFlag;

}
