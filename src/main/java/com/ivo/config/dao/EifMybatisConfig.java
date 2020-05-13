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
@MapperScan(basePackages = "com.ivo.rest.eif.mapper", sqlSessionFactoryRef = "eifSqlSessionFactory")
public class EifMybatisConfig {

    private DataSource eifDatasource;

    @Autowired
    public EifMybatisConfig(@Qualifier("eifDataSource") DataSource eifDatasource) {
        this.eifDatasource = eifDatasource;
    }

    @Bean
    public SqlSessionFactory eifSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(eifDatasource);
        bean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath*:com/ivo/rest/eif/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate eifSqlSessionTemplate(@Qualifier("eifSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
