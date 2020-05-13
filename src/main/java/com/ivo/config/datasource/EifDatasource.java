package com.ivo.config.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * EIF数据库
 * @author wj
 * @version 1.0
 */
@Configuration
public class EifDatasource {

    @Bean
    @ConfigurationProperties("spring.datasource.eifdb")
    public DataSource eifDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
