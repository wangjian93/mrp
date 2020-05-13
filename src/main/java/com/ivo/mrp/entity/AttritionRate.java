//package com.ivo.mrp.entity;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.ivo.common.model.AutoIncreaseEntityModel;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.Entity;
//import javax.persistence.Table;
//import java.util.Date;
//
///**
// * 损耗率
// * @author wj
// * @version 1.0
// */
//@Entity
//@Table(name = "MRP_AttritionRate")
//@Setter
//@Getter
//public class AttritionRate extends AutoIncreaseEntityModel {
//
//    /**
//     * 供应商
//     */
//    private String vender;
//
//    /**
//     * 供应商code
//     */
//    private String venderCode;
//
//    /**
//     * model
//     */
//    private String venderModel;
//
//    /**
//     * 厂商的料号
//     */
//    private String venderMaterial;
//
//    /**
//     * 物料组
//     */
//    private String materialGroup;
//
//    /**
//     * 龙腾料号
//     */
//    private String material;
//
//    /**
//     * 损耗率
//     */
//    private Double attritionRate;
//
//    /**
//     * 生效日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date effectDate;
//
//    /**
//     * 失效日期
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date expireDate;
//
//    /**
//     * 说明
//     */
//    private String memo;
//}
