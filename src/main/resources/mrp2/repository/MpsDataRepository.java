package com.ivo.mrp2.repository;

import com.ivo.mrp2.entity.MpsData;
import com.ivo.mrp2.key.MpsDataKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface MpsDataRepository extends JpaRepository<MpsData, MpsDataKey> {

    List<MpsData> findByVer(String ver);
}
