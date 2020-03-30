package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资源查询数据权限监听器
 * @FileName ResourceQueryDataPowerListener
 * @Author eTeng
 * @Date 2020/1/4 14:34
 * @Description
 */
@Slf4j
@Component
public class ResourceQueryDataPowerListener implements DataPowerListener {

    @Autowired
    private UmsRoleService umsRoleService;

    @Autowired
    private UmsRoleResourceService umsRoleResourceService;

    @Autowired
    private UmsUserService umsUserService;

    @Autowired
    private DataManager dataManager;

    @Override
    public boolean match(String powerType) {
        return "resource".equals(powerType);
    }

    @Override
    public OperatingOpportunity opportunity() {
        return OperatingOpportunity.BEFORE;
    }

    @Override
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) throws Exception {
        if(DynamicPower.OperatorType.QUERY.equals(opr)){
            UmsUserDTO user = umsUserService.currentUser();
            List<UmsRoleDTO> umsRoleDTOS = umsRoleService.queryAll(UmsRoleQueryCriteria.builder().build());
            // 得到查询参数
            UmsResourceQueryCriteria criteria = OperatingDataHelper.param(UmsResourceQueryCriteria.class,operatingData,false);
            if (CollUtil.isEmpty(umsRoleDTOS)) {
                log.warn("当前用户未拥有角色，用户id为:{}",user.getId());
                // 补充永远不能满足的条件
                criteria.setIds(CollUtil.newArrayList("NO"));
                return OperatingResult.builder().isContinue(true).build();
            }
            // 查询缓存数据
            List<UmsResourceDTO> cacheable = this.cacheable(umsRoleDTOS, operatingData);
            if(ObjectUtil.isNotNull(cacheable)){
                // 如果存在缓存，直接返回
                return OperatingResult.builder().isContinue(false).data(cacheable).build();
            }
            // 获取id
            Set<String> roleIds = umsRoleDTOS.stream().map(r -> r.getId()).collect(Collectors.toSet());
            Collection<UmsResourceDTO> resourceDTOS = umsRoleResourceService.findResourceByRoleId(roleIds);
            if(CollUtil.isEmpty(resourceDTOS)){
                // 补充永远不能满足的条件
                resourceDTOS.add(new UmsResourceDTO("NO",null,null,null,null,null,null,null));
            }
            // 补充数据权限
            criteria.setIds(resourceDTOS.stream().map(r -> r.getId()).collect(Collectors.toSet()));
            log.info("查询用户的资源数据时，进行数据权限控制。当前的用户id为:{}",user.getId());
        }
        return OperatingResult.builder().isContinue(true).build();
    }

    /**
     * 是否存在缓存
     * @param umsRoleDTOS 当前用户持有的角色
     * @param operatingData 调用目标方法的参数
     * @return
     * @throws Exception
     */
    private List<UmsResourceDTO> cacheable(List<UmsRoleDTO> umsRoleDTOS,Object... operatingData)throws Exception{
        boolean nonHasCache;
        List<UmsResourceDTO> ret = null;
        boolean pageable = OperatingDataHelper.pageable(false,operatingData);
        if (!pageable) {
            // 不是分页查询
            ret = CollUtil.newArrayList();
            for (UmsRoleDTO roleDTO : umsRoleDTOS) {
                Collection<UmsResourceDTO> resourceDTOS = dataManager.getResource("resource:", roleDTO.getId());
                nonHasCache = resourceDTOS == null;
                if(!nonHasCache){
                    // 将结果收集
                    ret.addAll(resourceDTOS);
                }else{
                    // 存在没有缓存资源的角色
                    return null;
                }
            }
        }
        return ret;
    }
}
