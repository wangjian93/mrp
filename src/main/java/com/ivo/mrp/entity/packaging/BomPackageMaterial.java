package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * CELL的包材BOM的材料明细
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Package_Material")
public class BomPackageMaterial extends AutoIncreaseEntityModel  {

    /**
     * 包材的ID
     */
    private String packageId;

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
     * 包装规格（pcs/每套）
     */
    private String specQty;

    /**
     * 损耗率
     */
    private String lossRate;

    /**
     * 单耗量（连片）:包装规格/连片数/Panel数目
     */
    private Double consumeQty;

    /**
     * 供应商标识，是否发给供应商
     */
    private boolean supplierFlag;

    private String memo = "";

    private String creator;

    private Date createDate = new Date();
}
