package com.ivo.mrp.entity.direct;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 供应商与料号关联
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Supplier_Material")
public class SupplierMaterial extends AutoIncreaseEntityModel {

    /**
     * 供应商ID
     */
    private String supplierCode;

    /**
     * 料号
     */
    private String material;

    /**
     * 有效性标识
     */
    private boolean validFlag = true;

    /**
     * 备注
     */
    private String memo = "";

    /**
     * 创建者
     */
    private String creator = "";

    /**
     * 创建时间
     */
    private Date createDate = new Date();

    /**
     * 修改者
     */
    private String updater = "";

    /**
     * 修改时间
     */
    private Date updateDate = new Date();
}
