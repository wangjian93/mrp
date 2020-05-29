package com.ivo.mrp2.entity;
import com.ivo.mrp2.key.MrpMaterialPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * MRP数据的料号
 * @author wj
 * @version 1.0
 */
@Entity(name = "mrp_material")
@Setter
@Getter
@IdClass(MrpMaterialPrimaryKey.class)
public class MrpMaterial {

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

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
     * 机种（料号对应多个机种时使用'/'分隔）
     */
    private String products;

    /**
     * 厂别
     */
    private String plant;

    /**
     * 损耗率
     */
    private double lossRate;


    /**
     * 期初库存 (良品仓)
     */
    private double goodInventory;

    /**
     * 期初库存 (呆滞仓)
     */
    private double dullInventory;
}
