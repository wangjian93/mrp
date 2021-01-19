package com.ivo.mrp.entity.direct.ary;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * ARY的材料清单
 * 数据来源81数据库的MM_O_ARYBOM
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Ary2")
public class BomAry {

    /**
     * ID: product_material
     */
    @Id
    private String id;

    /**
     * 机种
     */
    private String product;

    /**
     * 料号
     */
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
     * 物料组名
     */
    private String materialGroupName;

    /**
     * 计量单位
     */
    private String measureUnit;

    /**
     * 使用数量
     */
    private double usageQty;

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
