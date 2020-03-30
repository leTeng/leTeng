package com.eteng.scaffolding.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.admin.repository.UmsRoleMenuRepository;
import com.eteng.scaffolding.admin.repository.UmsUserRepository;
import com.eteng.scaffolding.admin.service.UmsUserRoleService;
import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsUserMapper;
import com.eteng.scaffolding.common.annotation.GlobalTransaction;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.exception.EntityNotFoundException;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.common.util.QueryHelp;
import com.eteng.scaffolding.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author eTeng
* @date 2019-12-27
*/
@Service
@CacheConfig(cacheNames = "umsUser")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsUserServiceImpl implements UmsUserService {

    private final UmsUserRepository umsUserRepository;

    @Autowired
    private DataManager dataManager;

    private final UmsUserMapper umsUserMapper;

    @Autowired
    private UmsRoleMenuRepository umsRoleMenuRepository;

    @Autowired
    private UmsUserRoleService umsUserRoleService;

    public UmsUserServiceImpl(UmsUserRepository umsUserRepository, UmsUserMapper umsUserMapper) {
        this.umsUserRepository = umsUserRepository;
        this.umsUserMapper = umsUserMapper;
    }

    @Override
    @Cacheable
    public Page<UmsUserDTO> queryAll(UmsUserQueryCriteria criteria, Pageable pageable){
        Page<UmsUser> page = umsUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsUserMapper::toDto);
    }

    @Override
    @Cacheable
    public List<UmsUserDTO> queryAll(UmsUserQueryCriteria criteria){
        return umsUserMapper.toDto(umsUserRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsUserDTO findById(String id) {
        UmsUser umsUser = umsUserRepository.findById(id).orElseGet(UmsUser::new);
        ValidationUtil.isNull(umsUser.getId(),"UmsUser","id",id);
        return umsUserMapper.toDto(umsUser);
    }

    @Override
    @CacheEvict(allEntries = true)
    @GlobalTransaction(exchange = "transaction.global.exchange",routingKey = "transaction.global.post-queue")
    public UmsUserDTO create(UmsUserDTO resources){
            // 密码MD5加密
            resources.setCredentials(DigestUtil.bcrypt(resources.getCredentials()));
            if(umsUserRepository.findByUserNameAndDel (resources.getUserName(),resources.getDel()) != null){
                throw new EntityExistException("用户名已注册！");
            }
            if(umsUserRepository.findByTypeIdAndDel (resources.getTypeId(),resources.getDel()) != null){
                throw new EntityExistException(UmsUser.class,"TypeId,Del",resources.getTypeId(),resources.getDel());
            }
            if(umsUserRepository.findByPhoneAndDel (resources.getPhone(),resources.getDel()) != null){
                throw new EntityExistException("手机号已注册！");
            }
            UmsUser entity = umsUserMapper.toEntity(resources);
            return umsUserMapper.toDto(umsUserRepository.save(entity));
    }

    @Override
    @CacheEvict(allEntries = true)
    @GlobalTransaction(exchange = "transaction.global.exchange",routingKey = "transaction.global.put-queue")
    public UmsUserDTO update(UmsUserDTO resources) {
        UmsUser umsUser = umsUserRepository.findById(resources.getId()).orElseGet(UmsUser::new);
        ValidationUtil.isNull( umsUser.getId(),"UmsUser","id",resources.getId());
        UmsUser umsUser1 = null;
        umsUser1 = umsUserRepository.findByUserNameAndDel(resources.getUserName(),resources.getDel());
        if(umsUser1 != null && !umsUser1.getId().equals(umsUser.getId())){
            throw new EntityExistException("用户名已注册！");
        }
        umsUser1 = umsUserRepository.findByTypeIdAndDel(resources.getTypeId(),resources.getDel());
        if(umsUser1 != null && !umsUser1.getId().equals(umsUser.getId())){
            throw new EntityExistException(UmsUser.class,"type_id,del",resources.getTypeId(),resources.getDel());
        }
        umsUser1 = umsUserRepository.findByPhoneAndDel(resources.getPhone(),resources.getDel());
        if(umsUser1 != null && !umsUser1.getId().equals(umsUser.getId())){
            throw new EntityExistException("手机号已注册！");
        }
        UmsUser entity = umsUserMapper.toEntity(resources);
        umsUser.copy(entity);
        umsUserRepository.save(umsUser);
        return resources;
    }

    @Override
    @CacheEvict(allEntries = true)
    @GlobalTransaction(exchange = "transaction.global.exchange",routingKey = "transaction.global.delete-queue")
    public UmsUserDTO delete(String id) {
        UmsUserDTO umsUserDTO = umsUserMapper.toDto(umsUserRepository.getOne(id));
        umsUserRepository.deleteById(id);
        return umsUserDTO;
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsUserDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsUserDTO umsUser : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("创建时间", umsUser.getCreateTime());
            map.put("修改时间(默认和创建时间相同)", umsUser.getUpdateTime());
            map.put("是否删除(0表示未删除，其他表示已删除)", umsUser.getDel());
            map.put("登录/登录名称", umsUser.getUserName());
            map.put("密码", umsUser.getCredentials());
            map.put("手机号码", umsUser.getPhone());
            map.put("用户是否可用(1 表示可用，0表示禁用)", umsUser.getEnabled());
            map.put("用户是否不过期(1表示不过期，0表示过期)", umsUser.getAccountNonExpired());
            map.put("密码是否不过期(1表示不过期，0表示过期)", umsUser.getCredentialsNonExpired());
            map.put("用户是否不锁定(1表示不锁定，0表示锁定)", umsUser.getAccountNonLocked());
            map.put("角色Id", umsUser.getRoleId());
            map.put("最后登录时间", umsUser.getLastLogin());
            map.put("用户类型", umsUser.getType());
            map.put("用户类型id", umsUser.getTypeId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsUserDTO findByUserName(String userName) {
        UmsUser umsUser = umsUserRepository.findByUserName(userName);
        return Optional.ofNullable(umsUser).map(u -> umsUserMapper.toDto(u)).get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsUserDTO findByPhone(String phoneNum) {
        UmsUser umsUser = umsUserRepository.findByPhone(phoneNum);
        return Optional.ofNullable(umsUser).map(u -> umsUserMapper.toDto(u)).get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public Collection<String> loadAuthority(String userId) throws Exception{
        Collection<UmsRoleDTO> roles = dataManager.getRole("role:", userId);
        if(CollUtil.isEmpty(roles)){
            roles = Optional.ofNullable(roles).orElse(umsUserRoleService.getRoleByCurrUser(userId));
        }
        return Optional.ofNullable(roles)
                .map(rs -> rs.stream().map(r -> r.getId()).collect(Collectors.toSet()))
                .map(roleIds -> umsRoleMenuRepository.findMenuByRoleIds(roleIds))
                .map(menus -> menus.stream().map(menu -> menu.getValue()).collect(Collectors.toSet()))
                .orElse(CollUtil.newHashSet());
    }

    @Override
    public UmsUserDTO currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        UmsUser user = this.umsUserRepository.findByUserName(username);
        if(ObjectUtil.isNull(user)){
            throw new EntityNotFoundException(UmsUser.class,"username",username);
        }
        return umsUserMapper.toDto(user);
    }
}