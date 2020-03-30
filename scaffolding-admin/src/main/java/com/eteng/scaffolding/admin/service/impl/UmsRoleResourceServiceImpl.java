package com.eteng.scaffolding.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsResource;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsRoleResource;
import com.eteng.scaffolding.admin.repository.UmsRoleRepository;
import com.eteng.scaffolding.admin.service.UmsRoleService;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.bean.BaseServiceImpl;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.admin.repository.UmsRoleResourceRepository;
import com.eteng.scaffolding.admin.service.UmsRoleResourceService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleResourceQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsRoleResourceMapper;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

/**
* @author eTeng
* @date 2019-12-27
*/
@Service
@Slf4j
@CacheConfig(cacheNames = "umsRoleResource")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsRoleResourceServiceImpl extends BaseServiceImpl implements UmsRoleResourceService {

    private final UmsRoleResourceRepository umsRoleResourceRepository;
    @Autowired
    private UmsRoleRepository umsRoleRepository;

    private final UmsRoleResourceMapper umsRoleResourceMapper;

    public UmsRoleResourceServiceImpl(UmsRoleResourceRepository umsRoleResourceRepository, UmsRoleResourceMapper umsRoleResourceMapper) {
        super(umsRoleResourceRepository);
        this.umsRoleResourceRepository = umsRoleResourceRepository;
        this.umsRoleResourceMapper = umsRoleResourceMapper;
    }

    @Override
    @Cacheable
    public Page<UmsRoleResourceDTO> queryAll(UmsRoleResourceQueryCriteria criteria, Pageable pageable){
        Page<UmsRoleResource> page = umsRoleResourceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsRoleResourceMapper::toDto);
    }

    @Override
    @Cacheable
    public List<UmsRoleResourceDTO> queryAll(UmsRoleResourceQueryCriteria criteria){
        return umsRoleResourceMapper.toDto(umsRoleResourceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsRoleResourceDTO findById(String id) {
        UmsRoleResource umsRoleResource = umsRoleResourceRepository.findById(id).orElseGet(UmsRoleResource::new);
        ValidationUtil.isNull(umsRoleResource.getId(),"UmsRoleResource","id",id);
        return umsRoleResourceMapper.toDto(umsRoleResource);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsRoleResourceDTO create(UmsRoleResource resources) {
        if(umsRoleResourceRepository.findByRoleIdAndResourceIdAndDel (resources.getRoleId(),resources.getResourceId(),resources.getDel()) != null){
            throw new EntityExistException("重复分配角色菜单关系！");
        }
        return umsRoleResourceMapper.toDto(umsRoleResourceRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(UmsRoleResource resources) {
        UmsRoleResource umsRoleResource = umsRoleResourceRepository.findById(resources.getId()).orElseGet(UmsRoleResource::new);
        ValidationUtil.isNull( umsRoleResource.getId(),"UmsRoleResource","id",resources.getId());
        UmsRoleResource umsRoleResource1 = null;
         umsRoleResource1 = umsRoleResourceRepository.findByRoleIdAndResourceIdAndDel(resources.getRoleId(),resources.getResourceId(),resources.getDel());
         if(umsRoleResource1 != null && !umsRoleResource1.getId().equals(umsRoleResource.getId())){
             throw new EntityExistException("重复分配角色菜单关系！");
         }
        umsRoleResource.copy(resources);
        umsRoleResourceRepository.save(umsRoleResource);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void delete(String id) {
        umsRoleResourceRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsRoleResourceDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsRoleResourceDTO umsRoleResource : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" createTime",  umsRoleResource.getCreateTime());
            map.put(" updateTime",  umsRoleResource.getUpdateTime());
            map.put(" del",  umsRoleResource.getDel());
            map.put("角色id", umsRoleResource.getRoleId());
            map.put("资源id", umsRoleResource.getResourceId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.RELATED,powerType = "resource")
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void bindingResource(OneToManyForm oneToManyForm) {
        @NotBlank(message = "绑定的id不能为空") String roleId = oneToManyForm.getOne();
        Collection<String> resourceIds = oneToManyForm.getMany();
        if (CollUtil.isEmpty(resourceIds)) {
            log.info("角色绑定的资源为空，角色id为:{}",roleId);
        }
        this.binding(roleId, resourceIds, rr -> rr.getResourceId().getId(), new EntityBeanFacotory<UmsRoleResource, String>() {
            @Override
            public Collection<UmsRoleResource> getBean(String id) {
                return umsRoleRepository.getOne(roleId).getRoleResources();
            }

            @Override
            public UmsRoleResource create(String many) {
                UmsRoleResource roleResource = new UmsRoleResource();
                UmsRole role = new UmsRole();
                role.setId(roleId);
                roleResource.setRoleId(role);
                UmsResource resource = new UmsResource();
                resource.setId(many);
                roleResource.setResourceId(resource);
                return roleResource;
            }
        });
    }

    @Override
    public Collection<UmsResourceDTO> findResourceByRoleId(Collection<String> roleIds) {
        return umsRoleResourceRepository.findResourceByRoleIds(roleIds);
    }

    @Override
    public Collection<UmsRoleResourceDTO> findByRoleIds(Collection<String> roleIds) {
        List<UmsRole> roles = roleIds.stream().map(roleId -> {
            UmsRole role = new UmsRole();
            role.setId(roleId);
            return role;
        }).collect(Collectors.toList());
        return umsRoleResourceMapper.toDto(umsRoleResourceRepository.findByRoleIdIn(roles));
    }

    @Override
    public Collection<UmsRoleDTO> findRoleByResourceId(String resourceId) {
        return umsRoleResourceRepository.findRoleByResourceId(resourceId);
    }
}