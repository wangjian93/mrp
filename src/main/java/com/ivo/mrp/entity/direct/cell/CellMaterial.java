package com.ivo.mrp.entity.direct.cell;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * CELL料号的材料,数据来源oracle的V_BOM_CELL_C
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Cell_Material")
public class CellMaterial extends AutoIncreaseEntityModel {

    public final static String TYPE_M = "主材";
    public final static String TYPE_P = "包材";


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

    private boolean validFlag = true;

    /**
     * 材料类型
     */
    private String type;

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
