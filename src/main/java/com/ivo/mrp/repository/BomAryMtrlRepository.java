package com.ivo.mrp.repository;

import com.ivo.mrp.entity.direct.ary.BomAryMtrl;
import com.ivo.mrp.key.BomAryMtrlKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface BomAryMtrlRepository extends JpaRepository<BomAryMtrl, BomAryMtrlKey> {

    /**
     * 筛选机种、是否使用
     * @param product 机种
     * @param useFlag 是否使用
     * @return  List<BomAryMtrl>
     */
    List<BomAryMtrl> findByProductAndUseFlag(String product, boolean useFlag);
}
