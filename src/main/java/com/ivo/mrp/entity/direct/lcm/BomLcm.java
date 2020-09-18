package com.ivo.mrp.entity.direct.lcm;

import com.ivo.mrp.key.MaterialKey;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * LCM的材料清单
 * 数据来源81数据库的MM_V_BOM_M1、MM_V_BOM_M2
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Lcm")
@IdClass(MaterialKey.class)
public class BomLcm {

    /**
     * 厂别 LCM1/LCM2
     */
    @Id
    private String fab;

    /**
     * 机种（成品料号）
     */
    @Id
    private String product;

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
     * 替代料比例
     */
    @Formula("(select DISTINCT s.substitute_rate from mrp3_substitute s where s.fab=fab and s.product=product and s.material_group=material_group and s.material=material and s.valid_flag=1)")
    private Double substituteRate;


    /**
     * 备注
     */
    private String memo = "同步自81数据库";

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
