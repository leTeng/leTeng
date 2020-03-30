package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重新绑定角色菜单关系数据权限监听器
 * @FileName ResetRoleMenuDataPowerListener
 * @Author eTeng
 * @Date 2020/1/6 11:33
 * @Description
 */
@Slf4j
@Component
public class ResetRoleMenuDataPowerListener implements DataPowerListener {

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
        if(DynamicPower.OperatorType.RELATED.equals(opr)){
            OneToManyForm param = OperatingDataHelper.param(OneToManyForm.class, operatingData,true);
            if (ObjectUtil.isNotNull(param)) {
                String roleId = param.getOne();
                dataManager.removeMenu("menu:",roleId);
                log.info("重新分配角色的菜单时，清空角色的菜单缓存数据成功，角色id为：{}",roleId);
            }
        }
        return OperatingResult.builder().isContinue(true).build();
    }
}
