package com.ivo.mrp.entity.direct.cell;

import com.ivo.mrp.key.MrpMaterialKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Cell_Material")
@IdClass(MrpMaterialKey.class)
public class MrpCellMaterial {

    /**
     * MRP版本
     */
    @Id
    private String ver;

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
     * 厂别
     */
    private String fab;

    /**
     * 损耗率
     */
    private double lossRate;

    /**
     * 期初库存 (良品仓)
     */
    private double goodInventory;

    /**
     * 哪一天作为期初库存
     */
    private java.sql.Date inventorDate;

    /**
     * 期初库存 (呆滞仓)
     */
    private double dullInventory;

    /**
     * 机种（料号对应多个机种时使用','分隔）
     */
    private String products;

    /**
     * 供应商（供应商对应多个机种时使用','分隔）
     */
    private String suppliers;
    private String supplierCodes;

    /**
     * 应用类型 （多个时使用','分隔）
     */
    private String applications;

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
