package com.ivo.mrp.entity.pol;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
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
public class BomPol extends AutoIncreaseEntityModel {

    public static final String Type_CF = "CF";
    public static final String Type_TFT = "TFT";

    /**
     * 厂别
     */
    private String fab;

    /**
     * 机种
     */
    private String product;

    /**
     * 料号
     */
    private String material;

    /**
     * 材料名
     */
    private String materialName;

    /**
     * 物料组
     */
    private String materialGroup;

    /**
     * CF/TFT
     */
    private String type;

    /**
     * 组
     */
    private String group;
}
