package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 物料组
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "mrp2_material_group")
@Setter
@Getter
public class MaterialGroup {

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
     * 创建时间
     */
    private Date createDate = new Date();

    /**
     * 备注
     */
    private String memo;
}
