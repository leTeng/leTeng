package com.eteng.scaffolding.admin.component.listener;

import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.vo.OneToManyForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 重新绑定用户角色关系数据权限监听器
 * @FileName ResetUserRoleDataPowerListener
 * @Author eTeng
 * @Date 2020/1/3 15:58
 * @Description
 */
@Slf4j
@Component
public class ResetUserRoleDataPowerListener implements DataPowerListener {

    @Autowired
    private DataManager dataManager;

    @Override
    public boolean match(String powerType) {
        return "role".equals(powerType);
    }

    @Override
    public OperatingOpportunity opportunity() {
        return OperatingOpportunity.AFTER;
    }

    @Override
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) throws Exception {
        if(DynamicPower.OperatorType.RELATED.equals(opr)){
            try{
                OneToManyForm param = OperatingDataHelper.param(OneToManyForm.class, operatingData,true);
                if (param != null) {
                    Collection<String> userId = param.getMany();
                    String [] uidArr = new String[userId.size()];
                    /*
                     * 清空目标用户的角色缓存数据，这里存在一个问题。他只会给新分配的用户清空已缓存的角色数据，如果没有分配的用户
                     * 假如之前就缓存到了该角色，是无法实现更新的。但是我们有自动过期机制，可以实现自动更新。实际会保证最终一致性。
                     */
                    dataManager.removeRole( "role:", userId.toArray(uidArr));
                    log.info("重新分配角色的用户，清空用户的角色缓存数据成功，用户id为：{}",userId.toString());
                }
            }catch (Exception var1){
                log.error("重新绑定用户角色关系时，更新数据权限异常。异常信息为：{}",var1.getMessage());
            }
        }
        return OperatingResult.builder().isContinue(true).build();
    }
}
