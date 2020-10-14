package com.ivo.mrp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * MRP版本
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mrp_Ver")
public class MrpVer {

    public static final String Type_Lcm = "LCM";
    public static final String Type_Ary = "ARY";
    public static final String Type_Cell = "CELL";
    public static final String Type_Package = "包材";
    public static final String Type_Pol = "POL";

    /**
     * MRP版本
     */
    @Id
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 类型
     */
    private String type;

    /**
     * 开始日期
     */
    private java.sql.Date startDate;

    /**
     * 结束日期
     */
    private java.sql.Date endDate;

    /**
     * dps版本
     */
    private String dpsVer;

    /**
     * mps版本
     */
    private String mpsVer;

    /**
     * 有效性
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate = new Date();

    /**
     * 修改者
     */
    private String updater = "";

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate = new Date();

}
