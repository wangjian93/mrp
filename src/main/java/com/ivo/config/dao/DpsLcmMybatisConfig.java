package com.ivo.config.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * DPS DB Mybatis配置
 * @author wj
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = "com.ivo.rest.dpsLcm", sqlSessionFactoryRef = "dpsLcmSqlSessionFactory")
public class DpsLcmMybatisConfig {

    private DataSource lcmDpsDatasource;

    @Autowired
    public DpsLcmMybatisConfig(@Qualifier("lcmDpsDatasource") DataSource lcmDpsDatasource) {
        this.lcmDpsDatasource = lcmDpsDatasource;
    }

    @Bean
    public SqlSessionFactory dpsLcmSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(lcmDpsDatasource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/ivo/rest/dpsLcm/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate dpsLcmSqlSessionTemplate(@Qualifier("dpsLcmSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
