package com.eteng.scaffolding.common.util;

import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;

/**
 * AOP工具
 * @FileName AspectUtil
 * @Author eTeng
 * @Date 2020/1/2 10:34
 * @Description
 */
public class AspectUtil {

    /**
     * 获取目标方法的指定注解
     * @param joinPoint
     * @param clz
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getMethodAnno(JoinPoint joinPoint, Class<T> clz){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if(ObjectUtil.isNotNull(signature)){
            return signature.getMethod().getDeclaredAnnotation(clz);
        }
        return null;
    }

    /**
     * 获取目标方法的参数
     * @param joinPoint
     * @return
     */
    public static Object[] getTargetMethodArgs(JoinPoint joinPoint){
        return joinPoint.getArgs();
    }
}
