package com.eteng.scaffolding.common.bean;

import com.eteng.scaffolding.common.util.ICollectionUtil;

import java.util.Collection;

/**
 * 基础服务接口
 * @FileName BaseService
 * @Author eTeng
 * @Date 2019/12/31 10:23
 * @Description
 */
public interface BaseService {

    /**
     * 绑定(分配)关系
     * @param one 一方
     * @param many 多方
     * @param extractor 识别符提取器
     * @param facotory 实体工厂
     * @param <T>
     * @return
     */
    <T extends BaseEntity> boolean binding(String one, Collection<String> many, ICollectionUtil.IdentifierExtractor<T,String> extractor, BaseServiceImpl.EntityBeanFacotory<T,String> facotory);

    /**
     * 绑定(分配)关系
     * @param one 一方
     * @param many 多方
     * @param extractor 识别符提取器
     * @param facotory 实体工厂
     * @param <T>
     * @return
     */
    <T extends BaseEntity> boolean binding(T one, Collection<T> many,ICollectionUtil.IdentifierExtractor<T,String> extractor ,BaseServiceImpl.EntityBeanFacotory facotory);
}
