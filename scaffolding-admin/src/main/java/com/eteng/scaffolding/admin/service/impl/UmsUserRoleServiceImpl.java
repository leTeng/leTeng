package com.eteng.scaffolding.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.admin.pojo.UmsUserRole;
import com.eteng.scaffolding.admin.repository.UmsRoleRepository;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.annotation.DynamicPowers;
import com.eteng.scaffolding.common.bean.BaseServiceImpl;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.admin.repository.UmsUserRoleRepository;
import com.eteng.scaffolding.admin.service.UmsUserRoleService;
import com.eteng.scaffolding.admin.service.dto.UmsUserRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserRoleQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsUserRoleMapper;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eteng.scaffolding.common.util.QueryHelp;

import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author eTeng
* @date 2019-12-27
*/
@Service
@Slf4j
@CacheConfig(cacheNames = "umsUserRole")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsUserRoleServiceImpl extends BaseServiceImpl  implements UmsUserRoleService {

    private final UmsUserRoleRepository umsUserRoleRepository;

    private final UmsUserRoleMapper umsUserRoleMapper;
    @Autowired
    private UmsRoleRepository umsRoleRepository;

    public UmsUserRoleServiceImpl(UmsUserRoleRepository umsUserRoleRepository, UmsUserRoleMapper umsUserRoleMapper) {
        super(umsUserRoleRepository);
        this.umsUserRoleRepository = umsUserRoleRepository;
        this.umsUserRoleMapper = umsUserRoleMapper;
    }

    @Override
    @Cacheable
    public Page<UmsUserRoleDTO> queryAll(UmsUserRoleQueryCriteria criteria, Pageable pageable){
        Page<UmsUserRole> page = umsUserRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsUserRoleMapper::toDto);
    }

    @Override
    @Cacheable
    public List<UmsUserRoleDTO> queryAll(UmsUserRoleQueryCriteria criteria){
        return umsUserRoleMapper.toDto(umsUserRoleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsUserRoleDTO findById(String id) {
        UmsUserRole umsUserRole = umsUserRoleRepository.findById(id).orElseGet(UmsUserRole::new);
        ValidationUtil.isNull(umsUserRole.getId(),"UmsUserRole","id",id);
        return umsUserRoleMapper.toDto(umsUserRole);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsUserRoleDTO create(UmsUserRole resources) {
        if(umsUserRoleRepository.findByUserIdAndRoleIdAndDel (resources.getUserId(),resources.getRoleId(),resources.getDel()) != null){
            throw new EntityExistException("重复分配用户角色关系！");
        }
        return umsUserRoleMapper.toDto(umsUserRoleRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(UmsUserRole resources) {
        UmsUserRole umsUserRole = umsUserRoleRepository.findById(resources.getId()).orElseGet(UmsUserRole::new);
        ValidationUtil.isNull( umsUserRole.getId(),"UmsUserRole","id",resources.getId());
        UmsUserRole umsUserRole1 = null;
         umsUserRole1 = umsUserRoleRepository.findByUserIdAndRoleIdAndDel(resources.getUserId(),resources.getRoleId(),resources.getDel());
         if(umsUserRole1 != null && !umsUserRole1.getId().equals(umsUserRole.getId())){
            throw new EntityExistException("重复分配用户角色关系！");
         }
        umsUserRole.copy(resources);
        umsUserRoleRepository.save(umsUserRole);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void delete(String id) {
        umsUserRoleRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsUserRoleDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsUserRoleDTO umsUserRole : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" createTime",  umsUserRole.getCreateTime());
            map.put(" updateTime",  umsUserRole.getUpdateTime());
            map.put(" del",  umsUserRole.getDel());
            map.put("用户id", umsUserRole.getUserId());
            map.put("角色id", umsUserRole.getRoleId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.RELATED,powerType = "role")
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void bindingUser(OneToManyForm oneToManyForm) {
        String id = oneToManyForm.getOne();
        Collection<String> userIds = oneToManyForm.getMany();
        boolean empty = CollUtil.isEmpty(userIds);
        if(empty){
            log.info("角色绑定空用户,角色id为：{}",oneToManyForm.getOne());
        }
        this.binding(id, userIds, ur -> ur.getUserId().getId() , new BaseServiceImpl.EntityBeanFacotory<UmsUserRole, String>() {
            @Override
            public Collection<UmsUserRole> getBean(String id) {
                // 查询已存在的关系
                return umsRoleRepository.getOne(id).getUserRoles();
            }

            @Override
            public UmsUserRole create(String many) {
                // 构建新的绑定关系
                UmsUserRole userRole = new UmsUserRole();
                UmsRole role = new UmsRole();
                role.setId(id);
                userRole.setRoleId(role);
                UmsUser user = new UmsUser();
                user.setId(many);
                userRole.setUserId(user);
                return userRole;
            }
        });
    }

    @Override
    public Collection<UmsUserDTO> getUserByRoleId(String roleId) {
        return umsUserRoleRepository.findUserByRoleId(roleId);
    }

    @Override
    public Collection<UmsRoleDTO> getRoleByCurrUser(String userId) {
        return umsUserRoleRepository.finRoledByUserId(userId);
    }
}