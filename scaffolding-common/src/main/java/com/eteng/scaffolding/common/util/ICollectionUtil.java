package com.eteng.scaffolding.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 简单的集合工具
 * @FileName EntityRelationHelper
 * @Author eTeng
 * @Date 2019/12/30 18:42
 * @Description
 */
public class ICollectionUtil {

    /**
     * 取不相交集合
     * @param target 目标集合
     * @param targetExtractor 目标集合元素识别符提取器
     * @param reference 参考集合
     * @param referenceExtractor 参考集合元素识别符提取器
     * @return
     */
    public static <O,T> Collection<O> intersect(Collection<T> target, IdentifierExtractor<T,O> targetExtractor, Collection<T> reference, IdentifierExtractor<T,O> referenceExtractor){
        Collection<O> targetSet = null;
        if(ObjectUtil.isNotNull(targetExtractor)){
            // 使用标识符提取器提取
            targetSet = extractor(target,targetExtractor);
        }
        return intersect(targetSet,reference,referenceExtractor);
    }

    /**
     * 取不相交集合
     * @param target 目标集合
     * @param reference 参考集合
     * @param extractor 识别符提取器
     * @return
     */
    public static <O,T> Collection<O> intersect(Collection<O> target, Collection<T> reference, IdentifierExtractor<T,O> extractor){
        Collection<O> referenceSet = null;
        if(ObjectUtil.isNotNull(extractor)){
            // 使用标识符提取器提取
            referenceSet = extractor(reference,extractor);
        }
        return intersect(target,referenceSet);
    }

    /**
     * 取不相交集合
     * @param target 目标集合
     * @param targetExtractor 目标集合元素识别符提取器
     * @param reference 参考集合
     * @return
     */
    public static <O,T> Collection<O> intersect(Collection<T> target, IdentifierExtractor<T,O> targetExtractor, Collection<O> reference){
        Collection<O> targetSet = null;
        if(ObjectUtil.isNotNull(targetExtractor)){
            // 使用标识符提取器提取
            targetSet = ICollectionUtil.extractor(target,targetExtractor);
        }
        return intersect(targetSet,reference);
    }

    /**
     * 取不相交集合
     * @param target 目标集合
     * @param reference 参考集合
     * @return
     */
    public static <O> Collection<O> intersect(Collection<O> target, Collection<O> reference){
        Collection<O> result = CollUtil.newArrayList();
        if(CollUtil.isEmpty(target)){
            // 如果目标集合为空，返回空不相交集合
            return result;
        }
        if(CollUtil.isEmpty(reference)){
            // 如果参考集合为空，返回全部的目标集合元素
            return target;
        }
        // 目标集合计数映射表
        Map<O, Integer> targetMap = CollUtil.countMap(target);
        // 参考集合计数映射表
        Map<O, Integer> referenceMap = CollUtil.countMap(reference);
        Set<O> targetKeys = targetMap.keySet();
        targetKeys.forEach(key -> {
            // 遍历寻找相交元素，如果出现null代表不相交
            Integer integer = referenceMap.get(key);
            if(integer == null){
                result.add(key);
            }
        });
        return result;
    }

    /**
     * 识别符提取器
     * @param <T>
     */
    public interface IdentifierExtractor<T,E>{
        /**
         * 提取识别符
         * @return
         */
        E extractor(T t);
    }

    /**
     * 提取识别符
     * @param col1
     * @param extractor
     * @param <T>
     * @return
     */
    public static  <T,E> Collection<E> extractor(Collection<T> col1, IdentifierExtractor<T,E> extractor){
        return col1.stream().map(r -> extractor.extractor(r)).collect(Collectors.toSet());
    }
}
