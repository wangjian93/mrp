package com.ivo.common.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 自增实体类模型
 * @author wj
 * @version 1.0
 */
@MappedSuperclass
public class AutoIncreaseEntityModel extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
