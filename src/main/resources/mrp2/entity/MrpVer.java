package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * MRP版本
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_ver")
public class MrpVer implements Serializable {

    public static final String STATUS_COMPUTE = "计算";
    public static final String STATUS_UPDATE = "更新";
    public static final String STATUS_COMP = "结束";

    /**
     * MRP版本
     */
    @Id
    private String mrpVer;

    /**
     * 周期开始
     */
    private java.sql.Date startDate;

    /**
     * 周期结束
     */
    private java.sql.Date endDate;

    /**
     * 选择的DPS版本
     */
    private String dpsVer;

    /**
     * 选择的Mps版本
     */
    private String mpsVer;

    /**
     * 厂别
     */
    private String plant;

    /**
     * 运算状态
     */
    private String status;

    /**
     * 创建日期
     */
    private Date createDate = new Date();

    /**
     * 版本号截取转数组
     * @return String[]
     */
    public String[] splitDpsVer(){
        if(dpsVer == null) return null;
        return this.dpsVer.split(",");
    }

    /**
     * 版本号截取转数组
     * @return String[]
     */
    public String[] splitMpsVer(){
        if(mpsVer == null) return null;
        return this.mpsVer.split(",");
    }
}
