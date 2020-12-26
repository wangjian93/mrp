package com.ivo.mrp.entity.pol;

import com.ivo.common.model.AutoIncreaseEntityModel;
import com.ivo.mrp.key.BomPolKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * POL材料BOM
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "MRP3_Bom_Pol")
@Setter
@Getter
@IdClass(BomPolKey.class)
public class BomPol {

    public static final String Type_CF = "CF";
    public static final String Type_TFT = "TFT";


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
     * 厂别
     */
    private String fab;

    /**
     * 按组分类排序
     */
    private String sort;
}
