package com.ivo.mrp.service.packageing;

import com.ivo.mrp.entity.packaging.BomPackage;
import com.ivo.mrp.entity.packaging.BomPackageMaterial;
import org.springframework.data.domain.Page;

import java.io.InputStream;
import java.util.List;

/**
 * 包材类BOM的服务接口
 * @author wj
 * @version 1.0
 */
public interface BomPackageService {

    /**
     * 导入机种的包材BOM
     * @param inputStream excel输入流
     * @param fileName 文件名
     */
    void importBomPackage(InputStream inputStream, String fileName);

    /**
     * 分页查询包材机种BOM
     * @param page 页数
     * @param limit 分页大小
     * @param searchProduct 机种
     * @return Page<BomPackage>
     */
    Page<BomPackage> queryBomPackage(int page, int limit, String searchProduct);

    BomPackage getBomPackageById(String id);

    List<BomPackageMaterial> getBomPackageMaterial(String packageId);

    /**
     * 获取机种的所有套包材
     * @param product 机种
     * @return  List<BomPackage>
     */
    List<BomPackage> getBomPackage(String product);

    /**
     * 获取包材类机种
     * @return List<String>
     */
    List<String> getPackageProduct();

    /**
     * 获取机种的一套包材
     * @param product 机种
     * @param type 单片/连片类型
     * @param linkQty 连片数
     * @return BomPackage
     */
    BomPackage getBomPackage(String product, String type, String linkQty);

    List<BomPackageMaterial> getBomPackageMaterial(String product, String type, String linkQty);

    List<BomPackageMaterial> getBomPackageForSupplier(String packageId);
}
