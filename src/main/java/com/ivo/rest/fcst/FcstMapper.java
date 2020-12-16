package com.ivo.rest.fcst;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wj
 * @version 1.0
 */
@Repository
public interface FcstMapper {

    /**
     * 获取MPS中的CELL BOM成品料号
     * @return List<Map>
     */
    List<Map> getBomCell();

    /**
     * 获取MPS中的Ary BOM的15料号
     * @return List<Map>
     */
    List<Map> getBomAry();


    /**
     * 获取CELL的MPS机种命名
     * @return
     */
    List<Map> getCellMpsMode();


    /**
     * 获取MPS的DateOfInsert作版本
     * @return List<String>
     */
    List<String> getMpsDateOfInsertForVersion();

    /**
     * 获取ARY MPS的DateOfInsert版本数据
     * @param dateOfInsert
     * @return  List<Map>
     */
    List<Map> getAryMpsDate(String dateOfInsert);

    /**
     * 获取CELL MPS的DateOfInsert版本数据
     * @param dateOfInsert
     * @return  List<Map>
     */
    List<Map> getCellMpsDate(String dateOfInsert);

    /**
     * 获取LCM的MPS
     * @param year 年份
     * @param month 月
     * @return List<Map>
     */
    List<Map> getMpsLcm(@Param("year") int year, @Param("month") int month);
}
