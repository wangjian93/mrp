package com.ivo.mrp.repository;

import com.ivo.mrp.entity.packaging.MrpPackage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpPackageRpository extends JpaRepository<MrpPackage, Long> {

    /**
     * 筛选版本、机种、单连片、连片数、切单模式
     * @param ver 版本
     * @param product 机种
     * @param type 单连片
     * @param linkQty 连片数
     * @param mode 切单模式
     * @return List<MrpPackage>
     */
    List<MrpPackage> findByVerAndProductAndTypeAndLinkQtyAndMode(String ver, String product, String type, Double linkQty, String mode);

    /**
     * 筛选版本
     * @param ver 版本
     * @return List<MrpPackage>
     */
    List<MrpPackage> findByVer(String ver, Sort sort);
}
