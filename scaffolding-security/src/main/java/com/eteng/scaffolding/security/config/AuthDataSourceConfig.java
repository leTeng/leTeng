package com.eteng.scaffolding.security.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @FileName SecurityDataSourceConfig.java
 * @Author eTeng
 * @Date 2019/12/16 12:47
 * @Description 权限库数据源配置
 */
@Configuration
public class AuthDataSourceConfig {

    /**
     * 配置数据源
     * @return
     */
    @Bean
    @Qualifier("authDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource authDataSource(){
        return DruidDataSourceBuilder.create().build();
    }
}
