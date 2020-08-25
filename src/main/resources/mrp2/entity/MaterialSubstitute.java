package com.ivo.mrp2.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ivo.mrp2.key.MaterialSubstituteKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * 替代料关系维护
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_material_substitute")
@IdClass(MaterialSubstituteKey.class)
public class MaterialSubstitute {

    /**
     * 厂别
     */
    @Id
    private String plant;

    /**
     * 机种
     */
    @Id
    private String product;

    /**
     * 料号
     */
    @Id
    private String material;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * 替代比例
     */
    private double substituteRate;

    /**
     * 物料描述
     */
    private String materialName;

    /**
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate = new Date();

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    /**
     * 备注
     */
    private String memo;

}
