package com.eteng.scaffolding.admin.service.impl;

import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.repository.UmsRoleRepository;
import com.eteng.scaffolding.admin.repository.UmsUserRoleRepository;
import com.eteng.scaffolding.admin.service.UmsRoleService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsRoleMapper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.annotation.DynamicPowers;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.common.util.QueryHelp;
import com.eteng.scaffolding.common.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
* @author eTeng
* @date 2019-12-27
*/
@Service
@CacheConfig(cacheNames = "umsRole")
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsRoleServiceImpl implements UmsRoleService {

    private final UmsRoleRepository umsRoleRepository;
    private final UmsRoleMapper umsRoleMapper;
    private UmsUserRoleRepository umsUserRoleRepository;


    public UmsRoleServiceImpl(UmsRoleRepository umsRoleRepository, UmsRoleMapper umsRoleMapper) {
        this.umsRoleRepository = umsRoleRepository;
        this.umsRoleMapper = umsRoleMapper;
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.QUERY,powerType = "role")
    public Page<UmsRoleDTO> queryAll(UmsRoleQueryCriteria criteria, Pageable pageable){
        Page<UmsRole> page = umsRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsRoleMapper::toDto);
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.QUERY,powerType = "role")
    public List<UmsRoleDTO> queryAll(UmsRoleQueryCriteria criteria){
        return umsRoleMapper.toDto(umsRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsRoleDTO findById(String id) {
        UmsRole umsRole = umsRoleRepository.findById(id).orElseGet(UmsRole::new);
        ValidationUtil.isNull(umsRole.getId(),"UmsRole","id",id);
        return umsRoleMapper.toDto(umsRole);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsRoleDTO create(UmsRoleDTO resources) {
        if(umsRoleRepository.findByNameAndTagAndDel (resources.getName(),resources.getTag(),resources.getDel()) != null){
            throw new EntityExistException("角色名称重复！");
        }
        UmsRole entity = umsRoleMapper.toEntity(resources);
        return umsRoleMapper.toDto(umsRoleRepository.save(entity));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(UmsRoleDTO resources) {
        UmsRole umsRole = umsRoleRepository.findById(resources.getId()).orElseGet(UmsRole::new);
        ValidationUtil.isNull( umsRole.getId(),"UmsRole","id",resources.getId());
        UmsRole umsRole1 = null;
         umsRole1 = umsRoleRepository.findByNameAndTagAndDel(resources.getName(),resources.getTag(),resources.getDel());
         if(umsRole1 != null && !umsRole1.getId().equals(umsRole.getId())){
             throw new EntityExistException("角色名称重复！");
         }
        UmsRole entity = umsRoleMapper.toEntity(resources);
        umsRole.copy(entity);
        umsRoleRepository.save(umsRole);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    @DynamicPower(opr = DynamicPower.OperatorType.DEL,powerType = "role")
    public void delete(String id) {
        umsRoleRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsRoleDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsRoleDTO umsRole : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" createTime",  umsRole.getCreateTime());
            map.put(" updateTime",  umsRole.getUpdateTime());
            map.put(" del",  umsRole.getDel());
            map.put("角色的名称(和角色的标签联合唯一)", umsRole.getName());
            map.put("角色的标签(和角色的名称联合唯一)", umsRole.getTag());
            map.put("角色的权限", umsRole.getPermission());
            map.put("角色的描述", umsRole.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}