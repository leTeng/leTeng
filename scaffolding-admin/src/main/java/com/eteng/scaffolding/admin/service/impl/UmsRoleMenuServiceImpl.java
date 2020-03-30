package com.eteng.scaffolding.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.pojo.UmsMenu;
import com.eteng.scaffolding.admin.pojo.UmsRole;
import com.eteng.scaffolding.admin.pojo.UmsRoleMenu;
import com.eteng.scaffolding.admin.pojo.UmsUser;
import com.eteng.scaffolding.admin.repository.UmsRoleRepository;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.bean.BaseServiceImpl;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.admin.repository.UmsRoleMenuRepository;
import com.eteng.scaffolding.admin.service.UmsRoleMenuService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleMenuQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsRoleMenuMapper;
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
@CacheConfig(cacheNames = "umsRoleMenu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsRoleMenuServiceImpl extends BaseServiceImpl implements UmsRoleMenuService {

    private final UmsRoleMenuRepository umsRoleMenuRepository;
    @Autowired
    private UmsRoleRepository umsRoleRepository;
    private final UmsRoleMenuMapper umsRoleMenuMapper;

    public UmsRoleMenuServiceImpl(UmsRoleMenuRepository umsRoleMenuRepository, UmsRoleMenuMapper umsRoleMenuMapper) {
        super(umsRoleMenuRepository);
        this.umsRoleMenuRepository = umsRoleMenuRepository;
        this.umsRoleMenuMapper = umsRoleMenuMapper;
    }

    @Override
    @Cacheable
    public Page<UmsRoleMenuDTO> queryAll(UmsRoleMenuQueryCriteria criteria, Pageable pageable){
        Page<UmsRoleMenu> page = umsRoleMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsRoleMenuMapper::toDto);
    }

    @Override
    @Cacheable
    public List<UmsRoleMenuDTO> queryAll(UmsRoleMenuQueryCriteria criteria){
        return umsRoleMenuMapper.toDto(umsRoleMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsRoleMenuDTO findById(String id) {
        UmsRoleMenu umsRoleMenu = umsRoleMenuRepository.findById(id).orElseGet(UmsRoleMenu::new);
        ValidationUtil.isNull(umsRoleMenu.getId(),"UmsRoleMenu","id",id);
        return umsRoleMenuMapper.toDto(umsRoleMenu);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsRoleMenuDTO create(UmsRoleMenu resources) {
        return umsRoleMenuMapper.toDto(umsRoleMenuRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(UmsRoleMenu resources) {
        UmsRoleMenu umsRoleMenu = umsRoleMenuRepository.findById(resources.getId()).orElseGet(UmsRoleMenu::new);
        ValidationUtil.isNull( umsRoleMenu.getId(),"UmsRoleMenu","id",resources.getId());
        umsRoleMenu.copy(resources);
        umsRoleMenuRepository.save(umsRoleMenu);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void delete(String id) {
        umsRoleMenuRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsRoleMenuDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsRoleMenuDTO umsRoleMenu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" createTime",  umsRoleMenu.getCreateTime());
            map.put(" updateTime",  umsRoleMenu.getUpdateTime());
            map.put(" del",  umsRoleMenu.getDel());
            map.put("菜单id", umsRoleMenu.getMenuId());
            map.put("角色id", umsRoleMenu.getRoleId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.RELATED,powerType = "menu")
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void bindingMenu(OneToManyForm oneToManyForm) {
        @NotBlank(message = "绑定的id不能为空") String roleId = oneToManyForm.getOne();
        Collection<String> menuIds = oneToManyForm.getMany();
        if (CollUtil.isEmpty(menuIds)) {
            log.info("角色绑定空的菜单列表，角色id为:{}",roleId);
        }
        this.binding(roleId, menuIds, rm -> rm.getMenuId().getId(), new EntityBeanFacotory<UmsRoleMenu, String>() {
            @Override
            public Collection<UmsRoleMenu> getBean(String id) {
                return umsRoleRepository.getOne(id).getRoleMenus();
            }

            @Override
            public UmsRoleMenu create(String many) {
                UmsRoleMenu roleMenu = new UmsRoleMenu();
                UmsRole role = new UmsRole();
                role.setId(roleId);
                roleMenu.setRoleId(role);
                UmsMenu menu = new UmsMenu();
                menu.setId(many);
                roleMenu.setMenuId(menu);
                return roleMenu;
            }
        });
    }

    @Override
    public Collection<UmsMenuDTO> findMenuByRoleId(String roleId) {
        return umsRoleMenuRepository.findMenuByRoleId(roleId);
    }

    @Override
    public Collection<UmsMenuDTO> findMenuByRoleId(Collection<String> roleIds) {
        return umsRoleMenuRepository.findMenuByRoleIds(roleIds);
    }

    @Override
    public Collection<UmsRoleMenuDTO> findByRoleIds(Collection<String> roleIds) {
        List<UmsRole> roles = roleIds.stream().map(roleId -> {
            UmsRole role = new UmsRole();
            role.setId(roleId);
            return role;
        }).collect(Collectors.toList());
        return umsRoleMenuMapper.toDto(umsRoleMenuRepository.findByRoleIdIn(roles));
    }

    @Override
    public Collection<UmsRoleDTO> findRoleByMenuId(String menuId) {
        return umsRoleMenuRepository.findRoleByMenuId(menuId);
    }
}