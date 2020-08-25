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
 * @author wj
 * @version 1.0
 */
@Configuration
@MapperScan(basePackages = "com.ivo.rest.fcst.mapper", sqlSessionFactoryRef = "fcstSqlSessionFactory")
public class FcstMybatisConfig {

    private DataSource fcstDatasource;

    @Autowired
    public FcstMybatisConfig(@Qualifier("fcstDataSource") DataSource fcstDatasource) {
        this.fcstDatasource = fcstDatasource;
    }

    @Bean
    public SqlSessionFactory fcstSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(fcstDatasource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/ivo/rest/fcst/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate fcstSqlSessionTemplate(@Qualifier("fcstSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
