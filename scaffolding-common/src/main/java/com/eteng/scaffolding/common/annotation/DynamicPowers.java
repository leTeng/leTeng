package com.eteng.scaffolding.common.annotation;

/**
 * 动态权限复合注解
 * @FileName DynamicPower
 * @Author eTeng
 * @Date 2020/1/2 10:02
 * @Description
 */
public @interface DynamicPowers {
    DynamicPower[] dynamicPower();
}
