package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.service.UmsRoleMenuService;
import com.eteng.scaffolding.admin.service.UmsRoleService;
import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.bo.SecurityUser;
import com.eteng.scaffolding.admin.service.dto.*;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单查询缓存数据权限监听器
 * @FileName MenuQueryCacheDataPowerListener
 * @Author eTeng
 * @Date 2020/1/5 13:53
 * @Description
 */
@Component
@Slf4j
public class MenuQueryCacheDataPowerListener implements DataPowerListener {

    @Autowired
    private UmsRoleService umsRoleService;

    @Autowired
    private UmsRoleMenuService umsRoleMenuService;

    @Autowired
    private UmsUserService umsUserService;

    @Autowired
    private DataManager dataManager;

    @Override
    public boolean match(String powerType) {
        return "menu".equals(powerType);
    }

    @Override
    public OperatingOpportunity opportunity() {
        return OperatingOpportunity.AFTER;
    }

    @Override
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) throws Exception {
        if(DynamicPower.OperatorType.QUERY.equals(opr)){
            // 非分叶查询才缓存数据
            if (!OperatingDataHelper.pageable(true,operatingData)) {
                UmsUserDTO user = umsUserService.currentUser();
                // 获取用户的拥有的角色
                List<UmsRoleDTO> umsRoleDTOS = umsRoleService.queryAll(UmsRoleQueryCriteria.builder().build());
                if (CollUtil.isEmpty(umsRoleDTOS)) {
                    log.warn("当前用户未拥有任何角色，无法缓存其菜单数据，用户id为：{}",user.getId());
                    return OperatingResult.builder().isContinue(true).build();
                }
                Set<String> roleIds = umsRoleDTOS.stream().map(r -> r.getId()).collect(Collectors.toSet());
                // 查询角色所拥有的全部菜单
                Collection<UmsRoleMenuDTO> roleMenus = umsRoleMenuService.findByRoleIds(roleIds);
                Map<String, Collection<UmsMenuDTO>> menuMap = MapUtil.newHashMap();
                roleMenus.forEach(rm -> {
                    // 通过角色给菜单分组
                    menuMap.merge(rm.getRoleId().getId(),CollUtil.newArrayList(rm.getMenuId()),(oldV,newV) -> {
                        oldV.addAll(newV);
                        return oldV;
                    });
                });
                this.cacheData(menuMap,roleIds);
                log.info("查询用户的菜单数据后，缓存用户的菜单数据。当前的用户id为:{}",user.getId());
            }
            return OperatingResult.builder().isContinue(false).data(operatingData[1]).build();
        }
        return OperatingResult.builder().isContinue(true).build();
    }

    private void cacheData(Map<String, Collection<UmsMenuDTO>> menuMap,Collection<String> roleIds){
        roleIds.forEach(k -> {
            try{
                /*
                 * 如果改角色未绑定菜单，那么缓存的时候缓存一个空的数组，表示该角色已经的菜单数据权限已缓存。
                 * 用于判断查询菜单的数据权限时候是否缓存的依据
                 */
                Collection<UmsMenuDTO> umsMenuDTOS = menuMap.getOrDefault(k, CollUtil.newArrayList());
                UmsMenuDTO [] menuArr = new UmsMenuDTO[umsMenuDTOS.size()];
                dataManager.putMenu("menu:",k,umsMenuDTOS.toArray(menuArr));
            }catch (Exception var1){
                log.error("缓存角色的菜单数据异常，异常信息为：{}",var1.getMessage());
            }
        });
    }
}
