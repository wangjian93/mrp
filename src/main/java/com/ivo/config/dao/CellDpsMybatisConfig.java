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
 * CELL DPS数据库Mybatis配置
 * @author wj
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = "com.ivo.rest.cellDps.mapper", sqlSessionFactoryRef = "cellDpsSqlSessionFactory")
public class CellDpsMybatisConfig {

    private DataSource dpsDatasource;

    @Autowired
    public CellDpsMybatisConfig(@Qualifier("cellDpsDataSource") DataSource dpsDatasource) {
        this.dpsDatasource = dpsDatasource;
    }

    @Bean
    public SqlSessionFactory cellDpsSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dpsDatasource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/ivo/rest/cellDps/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate cellDpsSqlSessionTemplate(@Qualifier("cellDpsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
