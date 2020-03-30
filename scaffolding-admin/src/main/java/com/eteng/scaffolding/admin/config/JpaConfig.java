package com.eteng.scaffolding.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * @FileName JpaConfig
 * @Author eTeng
 * @Date 2019/12/16 12:52
 * @Description jpa配置
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        // 引用的管理器工厂
        entityManagerFactoryRef = "securityEntiryManagerFactoryBean",
        // 引用事务管理器
        transactionManagerRef = "securityTransactionManager",
        // 扫描Repository包
        basePackages = {"com.eteng.scaffolding.admin.repository","com.eteng.scaffolding.repository"}
)
@EnableJpaAuditing
public class JpaConfig {

    @Autowired
    @Qualifier("securityDataSource")
    private DataSource securityDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    /**
     * 实体管理器
     * @param securityEntiryManagerFactoryBean
     * @return
     */
    @Bean
    public EntityManager securityEntityManager(LocalContainerEntityManagerFactoryBean securityEntiryManagerFactoryBean){
        return securityEntiryManagerFactoryBean.getObject().createEntityManager();
    }

    /**
     * 实体管理器工厂bean
     * @param builder
     * @return
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean securityEntiryManagerFactoryBean(EntityManagerFactoryBuilder builder){
        return builder
                .dataSource(securityDataSource)
                .properties(jpaProperties.getProperties())
                .persistenceUnit("admin_persistence_unit")
                .packages("com.eteng.scaffolding.admin.pojo","com.eteng.scaffolding.pojo")
                .build();
    }

    /**
     * jpa事务管理器
     * @param securityEntiryManagerFactoryBean
     * @return
     */
    @Bean
    public PlatformTransactionManager securityTransactionManager(LocalContainerEntityManagerFactoryBean securityEntiryManagerFactoryBean){
        return new JpaTransactionManager(securityEntiryManagerFactoryBean.getObject());
    }
}
