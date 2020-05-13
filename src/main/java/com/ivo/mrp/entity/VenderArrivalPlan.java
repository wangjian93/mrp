//package com.ivo.mrp.entity;
//
//import com.ivo.common.model.AutoIncreaseEntityModel;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import java.util.List;
//
///**
// * 每月的供应商到货计划
// * @author wj
// * @version 1.0
// */
//@Getter
//@Setter
//@Entity
//@Table(name = "mrp_VenderArrivalPlan")
//public class VenderArrivalPlan extends AutoIncreaseEntityModel {
//
//    /**
//     * 供应商
//     */
//    private String vender;
//
//    /**
//     * Model
//     */
//    private String model;
//
//    /**
//     * 厂商的料号
//     */
//    private String vender_material;
//
//    /**
//     * 对应的龙腾料号
//     */
//    private String material;
//
//    /**
//     * 月份
//     */
//    private String month;
//
//    /**
//     * 每天的到货计划
//     */
//    @OneToMany( mappedBy="venderArrivalPlan", cascade = CascadeType.ALL)
//    private List<VenderArrivalPlanDetail> venderArrivalPlanDetailList;
//}
