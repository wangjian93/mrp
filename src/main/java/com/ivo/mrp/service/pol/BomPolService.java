package com.ivo.mrp.service.pol;

import com.ivo.mrp.entity.pol.BomPol;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * POL的BOM服务
 * @author wj
 * @version 1.0
 */
public interface BomPolService {

    /**
     * 分页查询POL的BOM
     * @param page 页数
     * @param limit 分页大小
     * @param searchProduct 查询机种
     * @return Page<BomPol>
     */
    Page<BomPol> queryBomPol(int page, int limit, String searchProduct);

    /**
     * 查询POL的BOM
     * @param searchProduct 查询机种
     * @return List<BomPol>
     */
    List<BomPol> queryBomPol(String searchProduct);

    /**
     * 导入POL BOM
     * @param inputStream excel输入流
     * @param fileName 文件名
     */
    void importBomPol(InputStream inputStream, String fileName);

    /**
     * 导出POL BOM
     * @param searchProduct 机种
     * @return Workbook
     */
    Workbook exportBomPol(String searchProduct);

    /**
     * 添加POL BOM
     * @param product 机种
     * @param list 材料集合
     */
    void saveBomPol(String product, List<Map> list);

    /**
     * 获取机种的POL BOM
     * @param product 机种
     * @return List<BomPol>
     */
    List<BomPol> getBomPol(String product);

    /**
     * 下载excel模板
     * @return Workbook
     */
    Workbook downloadBomPolExcel();


    List<String> getBomPolProduct(String searchProduct);
}
