package com.ivo.config.datasource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author wj
 * @version 1.0
 */
@Configuration
@Slf4j
public class OracleDatasource {

    @Bean
    @ConfigurationProperties("spring.datasource.oracle")
    public DataSource oracleDataSource() {
        return DruidDataSourceBuilder.create().build();
    }
}
