package com.eteng.scaffolding.admin.service.impl;

import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.admin.repository.UmsResourceRepository;
import com.eteng.scaffolding.admin.service.UmsResourceService;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsResourceQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsResourceMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eteng.scaffolding.common.util.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author eTeng
* @date 2019-12-27
*/
@Service
@CacheConfig(cacheNames = "umsResource")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsResourceServiceImpl implements UmsResourceService {

    private final UmsResourceRepository umsResourceRepository;

    private final UmsResourceMapper umsResourceMapper;

    public UmsResourceServiceImpl(UmsResourceRepository umsResourceRepository, UmsResourceMapper umsResourceMapper) {
        this.umsResourceRepository = umsResourceRepository;
        this.umsResourceMapper = umsResourceMapper;
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.QUERY,powerType = "resource")
    public Page<UmsResourceDTO> queryAll(UmsResourceQueryCriteria criteria, Pageable pageable){
        Page<UmsResource> page = umsResourceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsResourceMapper::toDto);
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.QUERY,powerType = "resource")
    public List<UmsResourceDTO> queryAll(UmsResourceQueryCriteria criteria){
        return umsResourceMapper.toDto(umsResourceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsResourceDTO findById(String id) {
        UmsResource umsResource = umsResourceRepository.findById(id).orElseGet(UmsResource::new);
        ValidationUtil.isNull(umsResource.getId(),"UmsResource","id",id);
        return umsResourceMapper.toDto(umsResource);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsResourceDTO create(UmsResourceDTO resources) {
        if(umsResourceRepository.findByUriAndMethodAndDel (resources.getUri(),resources.getMethod(),resources.getDel()) != null){
            throw new EntityExistException("资源已重复！");
        }
        UmsResource entity = umsResourceMapper.toEntity(resources);
        return umsResourceMapper.toDto(umsResourceRepository.save(entity));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(UmsResourceDTO resources) {
        UmsResource umsResource = umsResourceRepository.findById(resources.getId()).orElseGet(UmsResource::new);
        ValidationUtil.isNull( umsResource.getId(),"UmsResource","id",resources.getId());
        UmsResource umsResource1 = null;
         umsResource1 = umsResourceRepository.findByUriAndMethodAndDel(resources.getUri(),resources.getMethod(),resources.getDel());
         if(umsResource1 != null && !umsResource1.getId().equals(umsResource.getId())){
             throw new EntityExistException("资源已重复！");
         }
        UmsResource entity = umsResourceMapper.toEntity(resources);
        umsResource.copy(entity);
        umsResourceRepository.save(umsResource);
    }

    @Override
    @CacheEvict(allEntries = true)
    @DynamicPower(opr = DynamicPower.OperatorType.DEL,powerType = "resource")
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void delete(String id) {
        umsResourceRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsResourceDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsResourceDTO umsResource : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" createTime",  umsResource.getCreateTime());
            map.put(" updateTime",  umsResource.getUpdateTime());
            map.put(" del",  umsResource.getDel());
            map.put("资源路径", umsResource.getUri());
            map.put("资源的请求方法", umsResource.getMethod());
            map.put("资源类型", umsResource.getType());
            map.put("资源描述", umsResource.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}