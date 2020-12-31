package com.ivo.mrp.entity.direct.cell;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CELL料号的材料,数据来源oracle的V_BOM_CELL_C
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Cell_Material")
public class BomCellMaterial extends AutoIncreaseEntityModel {

    private String PLANT;

    private String PRODUCT;

    private String CELLMTRL;

    private String MTRL_ID;

    private String MEASURE_UNIT;

    private double USAGEQTY;

    private int SUBFLAG;

    private String  MEMO;

    private String materialName;

    private String materialGroup;

    private String materialGroupName;

    private boolean validFlag;

    /**
     * 1主材
     * 2包材
     */
    private int type = 1;
}
