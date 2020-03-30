package com.eteng.scaffolding.common.annotation;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 全局事务注解
 * @FileName GlobalTransaction
 * @Author eTeng
 * @Date 2020/3/13 15:20
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Transactional(rollbackFor = Throwable.class,transactionManager = "securityTransactionManager")
public @interface GlobalTransaction {

    /**
     * 转化器
     * @return
     */
    String exchange();

    /**
     * 路由键
     * @return
     */
    String routingKey();

    /**
     * 重试次数
     * @return
     */
    int maxRetry() default 3;
}
