package com.ivo.mrp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 物料组
 * 数据来自81数据库的表MM_O_MaterialGroup
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Material_Group")
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

    private String memo = "同步自81的表MM_O_MaterialGroup";

    private String creator = "SYS";

    private Date createDate = new Date();
}
