package com.ivo.common;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 批量保存更新
 * @author wj
 * @version 1.0
 */
@Repository
public interface BatchService {
    // 批量存储的方法
    <T> void batchInsert(List<T> list);

    // 批量更新的方法
    <T> void batchUpdate(List<T> list);
}
