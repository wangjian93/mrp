package com.ivo.mrp.service;

import com.ivo.mrp.entity.VenderArrivalPlan;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * 供应商到货计划服务接口
 * @author wj
 * @version 1.0
 */
public interface VenderArrivalPlanService {

    /**
     * 动态条件查询，根据月份，供应商，料号获取到货计划
     * @param month 月份 例'2020-02'
     * @param vender 供应商
     * @param material 料号
     * @return
     */
    List<VenderArrivalPlan> getVenderArrivalPlan(String month, String vender, String material);



    String uploadVenderArrivalPlan(InputStream inputStream, String fileName) throws Exception;

    List<VenderArrivalPlan> getVenderArrivalPlanByMonth(String month);


}
