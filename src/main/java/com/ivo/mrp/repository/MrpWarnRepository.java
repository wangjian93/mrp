package com.ivo.mrp.repository;

import com.ivo.mrp.entity.MrpWarn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MrpWarnRepository extends JpaRepository<MrpWarn, Long> {

    /**
     * 筛选MRP版本
     * @param ver MRP版本
     * @return List<MrpWarn>
     */
    List<MrpWarn> findByVerAndValidFlagIsTrue(String ver);
}
