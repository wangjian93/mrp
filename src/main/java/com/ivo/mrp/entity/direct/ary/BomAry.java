package com.ivo.mrp.entity.direct.ary;

import com.ivo.mrp.key.BomAryKey;
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
@IdClass(BomAryKey.class)
public class BomAry {

    /**
     * 厂别
     */
    @Id
    private String fab = "ARY";

    /**
     * 机种
     */
    private String project;

    /**
     * 机种 （MPS的命名CellInPut）
     */
    @Id
    private String product;

    /**
     * ary的15料号
     */
    @Id
    private String aryMtrl;

    private String materialName;

    private String materialGroup;

    private String materialGroupName;

    /**
     * 是否使用
     */
    private boolean useFlag = true;

    /**
     * 备注
     */
    private String memo = "同步自MPS";

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
