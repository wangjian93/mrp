//package com.ivo.mrp.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Getter;
//import lombok.Setter;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.IdClass;
//import java.sql.Date;
//
///**
// * DPS & MPS生产计划
// * @author wj
// * @version 1.0
// */
//@Setter
//@Getter
//@Entity(name = "mrp_dps")
//@IdClass(PrimaryKey.class)
//public class Dps {
//
//    /**
//     * DPS版本
//     */
//    @Id
//    @Column(name = "ver", nullable = false)
//    private String ver;
//
//    /**
//     * 工厂
//     */
//    @Id
//    @Column(name = "fab", nullable = false)
//    private String fab;
//
//    /**
//     * 产品
//     */
//    @Id
//    @Column(name = "product", nullable = false)
//    private String product;
//
//    /**
//     * Model
//     */
//    private String model;
//
//    /**
//     * 生产日期
//     */
//    @Id
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    @Column(name = "fabDate", nullable = false)
//    private Date fabDate;
//
//    /**
//     * 生产数量
//     */
//    private Double qty;
//}
