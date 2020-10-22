package com.ivo.mrp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * DPS版本
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Dps_Ver")
public class DpsVer {

    public static final String Source_MC = "由MC手动导入";
    public static final String Source_DPS = "从LCM DPS系统同步";
    public static final String Source_Cell = "解析PC Excel的Cell Input";
    public static final String Source_Array = "解析PC Excel的Array Input";

    public static final String Type_Lcm = "LCM";
    public static final String Type_Ary = "ARY";
    public static final String Type_Cell = "CELL";
    public static final String Type_Package = "包材";
    public static final String Type_Pol = "POL";


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
     * 修改的小版本
     */
    private String modifyVer = null;

    /**
     * 是否为修改版本
     */
    private boolean modifyFlag = false;

    /**
     * 有效性
     */
    private boolean validFlag = true;

    /**
     * PC提供的DPS excel文件版本
     */
    private String dpsFile;

    /**
     * DPS文件名
     */
    private String fileName;

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
