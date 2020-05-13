package com.ivo.common.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 自增实体类模型
 * @author wj
 * @version 1.0
 */
@Setter
@Getter
@MappedSuperclass
public class AutoIncreaseEntityModel extends EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
