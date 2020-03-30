package com.eteng.scaffolding.admin.component.listener;

import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.service.UmsRoleMenuService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @FileName MenuRemoveDataPowerListener
 * @Author eTeng
 * @Date 2020/1/6 11:01
 * @Description
 */
@Slf4j
@Component
public class MenuRemoveDataPowerListener implements DataPowerListener {

    @Autowired
    private UmsRoleMenuService umsRoleMenuService;
    @Autowired
    private DataManager dataManager;

    @Override
    public boolean match(String powerType) {
        return "menu".equals(powerType);
    }

    @Override
    public OperatingOpportunity opportunity() {
        return OperatingOpportunity.BEFORE;
    }

    @Override
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) throws Exception {
        if(DynamicPower.OperatorType.DEL.equals(opr)){
            String menuId = OperatingDataHelper.param(String.class, operatingData,false);
            Collection<UmsRoleDTO> roleDTOS = umsRoleMenuService.findRoleByMenuId(menuId);
            Set<String> roleIds = roleDTOS.stream().map(r -> r.getId()).collect(Collectors.toSet());
            String [] roleIdArr = new String[roleIds.size()];
            dataManager.removeMenu("menu:",roleIds.toArray(roleIdArr));
            log.info("删除菜单时，清空角色的菜单缓存数据成功，角色id列表为：{}",roleIds.toString());
        }
        return OperatingResult.builder().isContinue(true).build();
    }
}
