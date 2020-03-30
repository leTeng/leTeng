package com.eteng.scaffolding.common.bean;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.common.util.ICollectionUtil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Iterator;

/**
 * @FileName BaseServiceImpl
 * @Author eTeng
 * @Date 2019/12/31 10:26
 * @Description
 */
public abstract class BaseServiceImpl implements BaseService {

    private JpaRepository jpaRepository;

    public BaseServiceImpl(JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public <T extends BaseEntity> boolean binding(String one, Collection<String> many, ICollectionUtil.IdentifierExtractor<T,String> extractor, EntityBeanFacotory<T,String> facotory) {
        // 已经存在的关系
        Collection<T> already = facotory.getBean(one);
        // 要新增的关系
        Collection<String> adds = ICollectionUtil.intersect(many, already, extractor);
        Collection<T> addEntitys = CollUtil.newArrayList();
        Collection<T> delEntitys = CollUtil.newArrayList();
        adds.forEach(add ->addEntitys.add(facotory.create(add)));
        // 批量添加
        jpaRepository.saveAll(addEntitys);
        // 要删除的关系
        Collection<String> dels = ICollectionUtil.intersect(already,extractor,many);
        Iterator<T> $i = already.iterator();
        while ($i.hasNext()) {
            T next = $i.next();
            if (dels.contains(extractor.extractor(next))) {
                $i.remove();
                delEntitys.add(next);
            }
        }
        // 批量删除
        jpaRepository.deleteAll(delEntitys);
        return true;
    }

    @Override
    public <T extends BaseEntity> boolean binding(T one, Collection<T> many, ICollectionUtil.IdentifierExtractor<T,String> extractor, EntityBeanFacotory facotory) {
        return binding(one.getId(),ICollectionUtil.extractor(many,extractor),extractor,facotory);
    }

    /**
     * 关系实体工厂
     */
    public abstract class EntityBeanFacotory<T,R>{

        /**
         * 查找关系对象
         * @param id
         * @return
         */
        public abstract Collection<T> getBean(String id);

        /**
         * 创建关系对象
         * @return
         */
        public abstract T create(R many);
    }
}
