package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.service.UmsRoleResourceService;
import com.eteng.scaffolding.admin.service.UmsRoleService;
import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.dto.*;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源查询缓存数据权限监听器
 * @FileName ResourceQueryCacheDataPowerListener
 * @Author eTeng
 * @Date 2020/1/5 13:53
 * @Description
 */
@Slf4j
@Component
public class ResourceQueryCacheDataPowerListener implements DataPowerListener {

    @Autowired
    private UmsRoleService umsRoleService;

    @Autowired
    private UmsUserService umsUserService;

    @Autowired
    private UmsRoleResourceService umsRoleResourceService;

    @Autowired
    private DataManager dataManager;

    @Override
    public boolean match(String powerType) {
        return "resource".equals(powerType);
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
                // 获取用户的拥有的角色
                List<UmsRoleDTO> umsRoleDTOS = umsRoleService.queryAll(UmsRoleQueryCriteria.builder().build());
                UmsUserDTO user = umsUserService.currentUser();
                if (CollUtil.isEmpty(umsRoleDTOS)) {
                    log.warn("当前用户未拥有任何角色，无法缓存其资源数据，用户id为：{}",user.getId());
                    return OperatingResult.builder().isContinue(true).build();
                }
                Set<String> roleIds = umsRoleDTOS.stream().map(r -> r.getId()).collect(Collectors.toSet());
                // 查询角色所拥有的全部菜单
                Collection<UmsRoleResourceDTO> roleResources = umsRoleResourceService.findByRoleIds(roleIds);
                Map<String, Collection<UmsResourceDTO>> resourceMap = MapUtil.newHashMap();
                roleResources.forEach(rm -> {
                    // 通过角色给菜单分组
                    resourceMap.merge(rm.getRoleId().getId(),CollUtil.newArrayList(rm.getResourceId()),(oldV,newV) -> {
                        oldV.addAll(newV);
                        return oldV;
                    });
                });
                this.cacheData(resourceMap,roleIds);
                log.info("查询用户的资源数据后，缓存用户的资源数据。当前的用户id为:{}",user.getId());
            }
            return OperatingResult.builder().isContinue(false).data(operatingData[1]).build();
        }
        return OperatingResult.builder().isContinue(true).build();
    }

    private void cacheData(Map<String, Collection<UmsResourceDTO>> resourceMap,Collection<String> roleIds){
        roleIds.forEach(k -> {
            try{
                /*
                 * 如果改角色未绑定资源，那么缓存的时候缓存一个空的数组，表示该角色已经的资源数据权限已缓存。
                 * 用于判断查询资源的数据权限时候是否缓存的依据
                 */
                Collection<UmsResourceDTO> umsResourceDTOS = resourceMap.getOrDefault(k, CollUtil.newArrayList());
                UmsResourceDTO [] resourceArr = new UmsResourceDTO[umsResourceDTOS.size()];
                dataManager.putResource("resource:",k,umsResourceDTOS.toArray(resourceArr));
            }catch (Exception var1){
                log.error("缓存角色的资源数据异常，异常信息为：{}",var1.getMessage());
            }
        });
    }
}
