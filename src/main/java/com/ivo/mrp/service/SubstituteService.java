package com.ivo.mrp.service;

import com.ivo.mrp.entity.Substitute;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 替代料维护服务接口
 * @author wj
 * @version 1.0
 */
public interface SubstituteService {

    /**
     * 同步替代料
     */
    void syncSubstitute();

    /**
     * 获取材料的替代比例
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param material 料号
     * @return 材料的替代比例
     */
    Double getSubstituteRate(String fab, String product,  String materialGroup, String material);

    /**
     * 获取Substitute
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param material 料号
     * @return Substitute
     */
    Substitute getSubstitute(String fab, String product, String materialGroup, String material);

    /**
     * 获取物料组的Substitute集合
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @return List<Substitute>
     */
    List<Substitute> getSubstitute(String fab, String product, String materialGroup);

    /**
     * 分页查询
     * @param page 页数
     * @param limit 分页大小
     * @param searchFab 厂别
     * @param searchProduct 机种
     * @param searchMaterialGroup 物料组
     * @return Page<Substitute>
     */
    Page<Substitute> querySubstitute(int page, int limit, String searchFab, String searchProduct, String searchMaterialGroup);

    /**
     * 查询
     * @param searchFab 厂别
     * @param searchProduct 机种
     * @param searchMaterialGroup 物料组
     * @return List<Substitute>
     */
    List<Substitute> querySubstitute(String searchFab, String searchProduct, String searchMaterialGroup);

    /**
     * 保存替代料
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param mapList 料号替代比集合
     * @param user 用户
     */
    void saveSubstitute(String fab, String product,  String materialGroup, List<Map> mapList, String user);

    /**
     * 删除一组替代料
     * @param fab 厂别
     * @param product 机种
     * @param materialGroup 物料组
     * @param user 用户
     */
    void delSubstitute(String fab, String product,  String materialGroup, String user);

    /**
     * excel导入替代料数据
     * @param inputStream excel
     * @param fileName 文件名
     */
    void importExcel(InputStream inputStream, String fileName);

    /**
     * excel导出替代料数据
     */
    Workbook exportExcel(String fab, String product, String materialGroup);

    /**
     * 下载替代料Excel模板
     * @return Workbook
     */
    Workbook downloadExcel();
}
