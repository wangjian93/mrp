package com.ivo.mrp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Mps_Ver")
public class MpsVer {

    public static final String Source_MC = "由MC手动导入";
    public static final String Source_MPS = "从MPS系统同步";

    public static final String Type_Lcm = "LCM";
    public static final String Type_Ary = "ARY";
    public static final String Type_Cell = "CELL";

    /**
     * 版本
     */
    @Id
    private String ver;

    /**
     * 厂别
     */
    private String fab;

    /**
     * 开始日期
     */
    private java.sql.Date startDate;

    /**
     * 结束日期
     */
    private java.sql.Date endDate;

    /**
     * 来源
     */
    private String source;

    /**
     * 类型
     */
    private String type;

    /**
     * 有效性
     */
    private boolean validFlag = true;

    /**
     * PSI 的MPS对应的版本
     */
    private String mpsFile;

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
}
