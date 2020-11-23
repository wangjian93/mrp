package com.ivo.mrp.service;

import com.ivo.mrp.entity.ActualArrival;
import org.springframework.data.domain.Page;

import java.sql.Date;
import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface ActualArrivalService {

    /**
     * 同步实际到货数据
     */
    void syncActualArrival();

    /**
     * 同步某天的实际到货数据
     * @param fabDate 日期
     */
    void syncActualArrival(Date fabDate);

    /**
     * 获取某天的到货数据
     * @param fabDate 日期
     * @return List<ActualArrival>
     */
    List<ActualArrival> getActualArrival(Date fabDate);

    /**
     * 获取某天材料的到货数据
     * @param fabDate 日期
     * @param material 料号
     * @return List<ActualArrival>
     */
    List<ActualArrival> getActualArrival(Date fabDate, String material);

    /**
     * 获取某天材料的到货数量
     * @param fabDate 日期
     * @param material 料号
     * @return List<ActualArrival>
     */
    double getActualArrivalQty(Date fabDate, String material);


    /**
     * 获取工厂某天材料的到货数量
     * @param fabDate 日期
     * @param material 料号
     * @param fab 工厂  "LCM1/LCM2/ARY/CELL'
     * @return
     */
    double getActualArrivalQty(Date fabDate, String material, String fab);

    /**
     * 获取工厂某天材料的到货数据
     * @param fabDate 日期
     * @param material 料号
     * @param fab 工厂  "LCM1/LCM2/ARY/CELL'
     * @return
     */
    List<ActualArrival> getActualArrival(Date fabDate, String material, String fab);

    /**
     * 分页查询到货数据
     * @param page 页数
     * @param limit 分页大小
     * @param fabDate 日期
     * @param material 料号
     * @param supplierCode 供应商
     * @param fab 工厂
     * @return Page<ActualArrival>
     */
    Page<ActualArrival> queryActualArrival(int page, int limit, Date fabDate, String material, String supplierCode, String fab);
}
