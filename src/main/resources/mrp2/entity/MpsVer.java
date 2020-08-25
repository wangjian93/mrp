package com.ivo.mrp2.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * MPS版本
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "mrp2_mps_ver")
public class MpsVer {

    /**
     * MPS版本
     */
    @Id
    private String ver;

    /**
     * 工厂
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
     * 创建日期
     */
    private Date createDate;

    /**
     * 是否为最新
     */
    private boolean latestFlag;
}
