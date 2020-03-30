package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.service.UmsUserRoleService;
import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleQueryCriteria;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色查询权限控制
 * @FileName RoleQueryDataPowerListener
 * @Author eTeng
 * @Date 2020/1/3 9:06
 * @Description
 */
@Slf4j
@Component
public class RoleQueryDataPowerListener implements DataPowerListener {

   @Autowired
   private UmsUserRoleService umsUserRoleService;

    @Autowired
    private UmsUserService umsUserService;
   
   @Autowired
   private DataManager dataManager;
    @Override
    public boolean match(String powerType) {
        return "role".equals(powerType);
    }

    @Override
    public OperatingOpportunity opportunity() {
        return OperatingOpportunity.BEFORE;
    }

    @Override
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) throws Exception{
        if(opr.equals(DynamicPower.OperatorType.QUERY)){
            UmsUserDTO user = umsUserService.currentUser();
            // 查询缓存是否存在数据
            Collection<UmsRoleDTO> roles = dataManager.getRole("role:", user.getId());
            if(roles != null){
                // 如果查询到缓存结果直接返回。并且终止程序的向下执行，直接返回结果。
                return OperatingResult.builder().isContinue(false).data(roles).build();
            }
            UmsRoleQueryCriteria criteria = OperatingDataHelper.param(UmsRoleQueryCriteria.class,operatingData,false);
            // 如果没有查询到缓存数据，直接查询数据库，不过这里加上的数据权限条件。
            Collection<UmsRoleDTO> roleDTOS = umsUserRoleService.getRoleByCurrUser(user.getId());
            if(CollUtil.isEmpty(roleDTOS)){
                // 补充永远不能满足的条件
                roleDTOS.add(new UmsRoleDTO("NO",null,null,null,null,null,null,null));
            }
            Set<String> ids = roleDTOS.stream().map(r -> r.getId()).collect(Collectors.toSet());
            log.info("查询用户的角色数据时，进行数据权限控制。当前的用户id为:{}",user.getId());
            criteria.setIds(ids);
        }
        return OperatingResult.builder().isContinue(true).build();
    }
}
