package com.ivo.mrp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 机种
 * 数据来自81数据库的表BG_O_Project
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@Entity
@Table(name = "MRP3_Project")
public class Project {

    /**
     * 机种
     */
    @Id
    private String project;

    /**
     * 切片数
     */
    private Double cut;

    /**
     * 应用类型
     */
    private String application;

    /**
     * 尺寸
     */
    private String size;

    private String memo = "同步自81的表BG_O_Project";

    private String creator = "SYS";

    private Date createDate = new Date();
}
