package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Package_Supplier")
public class PackageSupplier extends AutoIncreaseEntityModel {

    public static final String MaterialType_BOX = "纸箱";
    public static final String MaterialType_TRAY = "TRAY";

    private String month;

    /**
     * 机种
     */
    private String product;

    /**
     * 单片/连片
     */
    private String type;

    /**
     * 连片数 (连片)
     */
    private String linkQty;

    /**
     * 材料类型：纸箱或TRAY
     */
    private String materialType;

    /**
     * 供应商ID
     */
    private String supplierCode;

    /**
     * 供应商Name
     */
    private String supplierName;

    /**
     * 分配比例
     */
    private double allocationRate;



    private boolean validFlag = true;

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

    private String updater = "SYS";

    private Date updateDate = new Date();
}
