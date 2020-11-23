package com.ivo.mrp.entity.direct.ary;

import com.ivo.mrp.key.BomAryMtrlKey;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

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
@Table(name = "MRP3_Bom_Ary_Mtrl")
@IdClass(BomAryMtrlKey.class)
public class BomAryMtrl {
    /**
     * 厂别
     */
    @Id
    private String fab;

    /**
     * 机种
     */
    private String project;

    @Id
    private String product;

    /**
     * ary的15料号
     */
    @Id
    private String aryMtrl;

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
     * 计量单位
     */
    private String measureUnit;

    /**
     * 使用数量
     */
    private double usageQty;

    /**
     * 是否使用
     */
    private boolean useFlag = true;

    /**
     * 替代料比例
     */
    @Formula("(select DISTINCT s.substitute_rate from mrp3_substitute s where s.fab=fab and s.product=project and s.material_group=material_group and s.material=material and s.valid_flag=1)")
    private Double substituteRate;

//    /**
//     * 切片数
//     */
//    @Formula("(select DISTINCT p.cut from mrp3_project p where p.project=project)")
//    private Double cut;

    /**
     * 备注
     */
    private String memo = "来自81数据库15料号串51料号";

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();

    /**
     * 修改者
     */
    private String updater = "SYS";

    /**
     * 修改时间
     */
    private Date updateDate = new Date();
}
