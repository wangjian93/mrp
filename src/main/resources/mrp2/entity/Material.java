package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 料号
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "mrp2_material")
@Setter
@Getter
public class Material {

    /**
     * 料号
     */
    @Id
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
     * 单位
     */
    private String measureUnit;

    /**
     * 创建时间
     */
    private Date createDate = new Date();

    /**
     * 备注
     */
    private String memo;
}
