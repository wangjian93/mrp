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
@MapperScan(basePackages = "com.ivo.rest.dpsAryCell", sqlSessionFactoryRef = "dpsAryCellSqlSessionFactory")
public class DpsAryCellMybatisConfig {

    private DataSource aryCellDpsDataSource;

    @Autowired
    public DpsAryCellMybatisConfig(@Qualifier("aryCellDpsDataSource") DataSource aryCellDpsDataSource) {
        this.aryCellDpsDataSource = aryCellDpsDataSource;
    }

    @Bean
    public SqlSessionFactory dpsAryCellSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(aryCellDpsDataSource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/ivo/rest/dpsAryCell/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate dpsAryCellSqlSessionTemplate(@Qualifier("dpsAryCellSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
