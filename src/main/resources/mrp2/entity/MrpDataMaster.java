package com.ivo.mrp2.entity;
import com.ivo.mrp2.key.MrpDataMasterPrimaryKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * MRP主数据，记录MRP的材料
 * @author wj
 * @version 1.0
 */
@Entity
@Table(name = "mrp2_data_master")
@Setter
@Getter
@IdClass(MrpDataMasterPrimaryKey.class)
public class MrpDataMaster {

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

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
     * 机种（料号对应多个机种时使用','分隔）
     */
    private String products;

    /**
     * 厂别
     */
    private String plant;

    /**
     * 损耗率
     */
    private double lossRate;

    /**
     * 期初库存 (良品仓)
     */
    private double goodInventory;

    /**
     * 期初库存 (呆滞仓)
     */
    private double dullInventory;

    /**
     * 供应商（供应商对应多个机种时使用','分隔）
     */
    private String supplier;

    /**
     * 创建日期
     */
    private Date createDate = new Date();
}
