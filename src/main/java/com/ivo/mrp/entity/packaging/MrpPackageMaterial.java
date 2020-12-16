package com.ivo.mrp.entity.packaging;

import com.ivo.mrp.key.MrpPackageMaterialKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Package_Material")
@IdClass(MrpPackageMaterialKey.class)
public class MrpPackageMaterial {

    /**
     * MRP版本
     */
    @Id
    private String ver;

    @Id
    private String packageId;

    /**
     * 机种
     */
    private String product;

    /**
     * 单片/连片
     */
    private String type;

    /**
     * 连片数
     */
    private String linkQty;

    /**
     * 抽单模式
     */
    private String mode;

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
     * 物料组名
     */
    private String materialGroupName;

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

    private String fab;
}
