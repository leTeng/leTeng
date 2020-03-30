package com.eteng.scaffolding.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态权限注解
 * @FileName DynamicPower
 * @Author eTeng
 * @Date 2020/1/2 10:02
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public  @interface DynamicPower {

    /**
     * 操作类型
     * @return
     */
    OperatorType opr();

    /**
     * 权限类型
     * @return
     */
    String powerType();

    enum OperatorType{
        /**
         * 查询操作
         */
        QUERY,
        /**
         * 添加操作
         */
        ADD,
        /**
         * 删除操作
         */
        DEL,
        /**
         * 关联操作
         */
        RELATED
    }
}
