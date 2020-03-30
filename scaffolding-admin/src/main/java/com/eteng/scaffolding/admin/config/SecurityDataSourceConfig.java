package com.eteng.scaffolding.admin.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @FileName SecurityDataSourceConfig.java
 * @Author eTeng
 * @Date 2019/12/16 12:47
 * @Description 权限库数据源配置
 */
@Configuration
public class SecurityDataSourceConfig {

    /**
     * 配置数据源
     * @return
     */
    @Bean
    @Primary
    @Qualifier("securityDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.security")
    public DataSource securityDataSource(){
        return DruidDataSourceBuilder.create().build();
    }
}
