package com.ivo.mrp.entity.direct.cell;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * CELL的材料BOM
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Cell2")
public class BomCell2 {

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

    /**
     * 替代料比例
     */
//    @Formula("(select DISTINCT s.substitute_rate from mrp3_substitute s where s.fab='CELL' and s.product=product and s.material_group=material_group and s.material=material and s.valid_flag=1)")
//    private Double substituteRate;
}
