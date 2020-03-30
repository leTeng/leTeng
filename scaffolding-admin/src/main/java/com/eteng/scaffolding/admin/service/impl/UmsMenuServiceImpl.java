package com.eteng.scaffolding.admin.service.impl;

import com.eteng.scaffolding.admin.pojo.UmsMenu;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.exception.EntityExistException;
import com.eteng.scaffolding.common.util.ValidationUtil;
import com.eteng.scaffolding.common.util.FileUtil;
import com.eteng.scaffolding.admin.repository.UmsMenuRepository;
import com.eteng.scaffolding.admin.service.UmsMenuService;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsMenuQueryCriteria;
import com.eteng.scaffolding.admin.service.mapper.UmsMenuMapper;
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
@CacheConfig(cacheNames = "umsMenu")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UmsMenuServiceImpl implements UmsMenuService {

    private final UmsMenuRepository umsMenuRepository;

    private final UmsMenuMapper umsMenuMapper;

    public UmsMenuServiceImpl(UmsMenuRepository umsMenuRepository, UmsMenuMapper umsMenuMapper) {
        this.umsMenuRepository = umsMenuRepository;
        this.umsMenuMapper = umsMenuMapper;
    }

    @Override
    @DynamicPower(opr = DynamicPower.OperatorType.QUERY,powerType = "menu")
    public Page<UmsMenuDTO> queryAll(UmsMenuQueryCriteria criteria, Pageable pageable){
        Page<UmsMenu> page = umsMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return page.map(umsMenuMapper::toDto);
    }

    @Override
    @Cacheable
    @DynamicPower(opr = DynamicPower.OperatorType.QUERY,powerType = "menu")
    public List<UmsMenuDTO> queryAll(UmsMenuQueryCriteria criteria){
        return umsMenuMapper.toDto(umsMenuRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public UmsMenuDTO findById(String id) {
        UmsMenu umsMenu = umsMenuRepository.findById(id).orElseGet(UmsMenu::new);
        ValidationUtil.isNull(umsMenu.getId(),"UmsMenu","id",id);
        return umsMenuMapper.toDto(umsMenu);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public UmsMenuDTO create(UmsMenuDTO resources) {
        if(umsMenuRepository.findByUriAndDel (resources.getUri(),resources.getDel()) != null){
            throw new EntityExistException("菜单已重复！");
        }
        UmsMenu entity = umsMenuMapper.toEntity(resources);
        return umsMenuMapper.toDto(umsMenuRepository.save(entity));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    public void update(UmsMenuDTO resources) {
        UmsMenu umsMenu = umsMenuRepository.findById(resources.getId()).orElseGet(UmsMenu::new);
        ValidationUtil.isNull( umsMenu.getId(),"UmsMenu","id",resources.getId());
        UmsMenu umsMenu1 = null;
        umsMenu1 = umsMenuRepository.findByUriAndDel(resources.getUri(),resources.getDel());
        if(umsMenu1 != null && !umsMenu1.getId().equals(umsMenu.getId())){
            throw new EntityExistException("菜单已重复！");
        }
        UmsMenu entity = umsMenuMapper.toEntity(resources);
        umsMenu.copy(entity);
        umsMenuRepository.save(umsMenu);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class,transactionManager = "securityTransactionManager")
    @DynamicPower(opr = DynamicPower.OperatorType.DEL,powerType = "menu")
    public void delete(String id) {
        umsMenuRepository.deleteById(id);
    }

    /**
     * 由于代码是自动生成的，如果代码出现中文是因为使用数据库的字段注释作为map的
     * key。如果使用导出功能请自己手动修改key。
     */
    @Override
    public void download(List<UmsMenuDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UmsMenuDTO umsMenu : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" createTime",  umsMenu.getCreateTime());
            map.put(" updateTime",  umsMenu.getUpdateTime());
            map.put(" del",  umsMenu.getDel());
            map.put("菜单名称", umsMenu.getName());
            map.put("菜单的路径", umsMenu.getUri());
            map.put("是否显示菜单(1为显示，0为不显示)", umsMenu.getEnable());
            map.put("上一级菜单", umsMenu.getParentId());
            map.put("菜单排序", umsMenu.getSort());
            map.put("菜单的权限值", umsMenu.getValue());
            map.put("菜单的类型(0为目录，1为菜单，2为按钮)", umsMenu.getType());
            map.put("菜单的图标", umsMenu.getIcon());
            map.put("菜单描述", umsMenu.getDescription());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}