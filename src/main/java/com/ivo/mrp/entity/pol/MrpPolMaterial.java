package com.ivo.mrp.entity.pol;

import com.ivo.mrp.key.MrpPolMaterialKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.sql.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Pol_Material")
@IdClass(MrpPolMaterialKey.class)
public class MrpPolMaterial {

    /**
     * MRP版本
     */
    @Id
    private String ver;

    /**
     * 厂别
     */
    private String fab;

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
     * 材料名
     */
    private String materialName;

    /**
     * 供应商
     */
    private String supplierCode;

    /**
     * 供应商名
     */
    private String supplierName;

    /**
     * CF/TFT/补偿膜
     */
    private String type;

    /**
     * 损耗率
     */
    private double lossRate;

    /**
     * 切片数
     */
    private Double cut;

    private double goodInventory;

    private double dullInventory;

    private Date inventoryDate;
}
