package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.service.UmsUserRoleService;
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
 * 删除角色更新数据权限监听器
 * @FileName RoleRemoveDataPowerListener
 * @Author eTeng
 * @Date 2020/1/3 11:04
 * @Description
 */
@Slf4j
@Component
public class RoleRemoveDataPowerListener implements DataPowerListener {

    @Autowired
    private UmsUserRoleService umsUserRoleService;
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
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) {
        if(ObjectUtil.isNotNull(opr) && opr.equals(DynamicPower.OperatorType.DEL)){
            // 获取方法的请求参数
            String roleId = OperatingDataHelper.param(String.class, operatingData,false);
            if(ObjectUtil.isNotNull(roleId)){
                    Collection<UmsUserDTO> userDTOs = umsUserRoleService.getUserByRoleId(roleId);
                    Set<String> uIds = userDTOs.stream().map(u -> u.getId()).collect(Collectors.toSet());
                    String [] idArr = new String[uIds.size()];
                    try{
                        // 删除已缓存的角色权限
                        dataManager.removeRole("role:",uIds.toArray(idArr));
                        log.info("删除角色时，清空用户的角色缓存数据成功，用户id列表为：{}",uIds.toString());
                        // TODO: 2020/1/8 删除已缓存的角色菜单数据权限

                        // TODO: 2020/1/8 删除已缓存的角色资源数据权限

                    }catch (Exception var1){
                        log.error("删除角色时更新数据权限错误，角色id为：{}",roleId);
                    }
            }
        }
        return OperatingResult.builder().isContinue(true).build();
    }
}
