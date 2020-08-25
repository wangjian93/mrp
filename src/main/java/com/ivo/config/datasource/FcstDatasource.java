package com.ivo.config.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author wj
 * @version 1.0
 */
@Configuration
public class FcstDatasource {

    @Bean
    @ConfigurationProperties("spring.datasource.fcstdb")
    public DataSource fcstDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
