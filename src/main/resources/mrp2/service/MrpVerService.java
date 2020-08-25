package com.ivo.mrp2.service;

import com.ivo.mrp2.entity.MrpVer;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * MRP版本服务
 * @author wj
 * @version 1.0
 */
public interface MrpVerService {

    /**
     * 分页查询MRP版本信息
     * @param page 页数
     * @param limit 分页大小
     * @param dpsVer DPS版本
     * @param mpsVer MPS版本
     * @param plant 厂别
     * @param fromDate 日期区间
     * @param toDate 日期区间
     * @return Page<MrpVer>
     */
    Page<MrpVer> getPageMrpVer(int page, int limit, String dpsVer, String mpsVer, String plant, Date fromDate, Date toDate);

    /**
     * 获取MRP版本对象
     * @param ver MRP版本号
     * @return MrpVer
     */
    MrpVer getMrpVer(String ver);

    /**
     * 保存MRP版本
     * @param mrpVer MRP版本对象
     */
    void saveMrpVer(MrpVer mrpVer);

    /**
     * 获取MRP的日历
     * @param ver MRP版本
     * @return List<Date>
     */
    List<Date> getMrpCalendarList(String ver);
}
