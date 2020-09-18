package com.ivo.mrp.entity.packaging;

import com.ivo.common.model.AutoIncreaseEntityModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 包材的供应商
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Supplier_Package")
public class SupplierPackage extends AutoIncreaseEntityModel {

    /**
     * 月份
     */
    private String month;

    /**
     * 机种
     */
    private String project;

    /**
     * 单片/连片类型
     */
    private String linkType;

    /**
     * 连片数
     */
    private Double linkQty;

    /**
     * 包装数量
     */
    private Double panelQty;

    /**
     * 厂别类型
     */
    private String type;

    /**
     * MODEL
     */
    private String model;

    /**
     * 切片数
     */
    private Double cut;

    @OneToMany(mappedBy="supplierPackage", cascade= CascadeType.ALL)
    private List<SupplierPackageDetail> supplierList = new ArrayList<>();

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
