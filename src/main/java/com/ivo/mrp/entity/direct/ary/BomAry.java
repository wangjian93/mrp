package com.ivo.mrp.entity.direct.ary;

import com.ivo.mrp.key.MaterialKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Date;

/**
 * ARY的材料清单
 * 数据来源81数据库的MM_O_ARYBOM
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Bom_Ary")
@IdClass(MaterialKey.class)
public class BomAry {

    /**
     * 厂别
     */
    @Id
    private String fab = "ARY";

    /**
     * 机种
     */
    @Id
    private String product;

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

//    /**
//     * 切片数
//     */
//    @Formula("(select DISTINCT p.cut from mrp3_project p where p.project=project)")
//    private Double cut;

    /**
     * 备注
     */
    private String memo = "同步自81数据库";

    /**
     * 创建者
     */
    private String creator = "SYS";

    /**
     * 创建时间
     */
    private Date createDate = new Date();
}
