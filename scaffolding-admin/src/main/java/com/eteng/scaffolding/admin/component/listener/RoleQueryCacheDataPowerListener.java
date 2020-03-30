package com.eteng.scaffolding.admin.component.listener;

import cn.hutool.core.util.ObjectUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.data.DataManager;
import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.admin.util.OperatingDataHelper;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 角色查询结果缓存监听器
 * @FileName RoleQueryCacheDataPowerListener
 * @Author eTeng
 * @Date 2020/1/3 11:20
 * @Description
 */
@Slf4j
@Component
public class RoleQueryCacheDataPowerListener implements DataPowerListener {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private UmsUserService umsUserService;

    @Override
    public boolean match(String powerType) {
        return "role".equals(powerType);
    }

    @Override
    public OperatingOpportunity opportunity() {
        return OperatingOpportunity.AFTER;
    }

    @Override
    public OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) {
        if(opr.equals(DynamicPower.OperatorType.QUERY)){
            Object[] objs = operatingData;
            UmsUserDTO user = umsUserService.currentUser();
            if(ObjectUtil.isNotNull(objs)){
                boolean pageable = OperatingDataHelper.pageable(true,operatingData);
                // 得到查询结果
                Object results = objs[1];
                if(pageable){
                    // 如果是分页查询不缓存查询结果
                    return OperatingResult.builder().isContinue(true).data(results).build();
                }
                // TODO: 2020/1/3 如果有其他的查询参数干扰查询的结果集，可以在这里进行判断是否将结果缓存
                if (results != null) {
                    try{
                        Collection<UmsRoleDTO> resultList = (Collection) results;
                        UmsRoleDTO [] resultArr = new UmsRoleDTO[resultList.size()];
                        // 缓存查询结果
                        dataManager.putRole("role:",user.getId(), resultList.toArray(resultArr));
                        log.info("查询用户的角色数据后，缓存用户的角色数据。当前的用户id为:{}",user.getId());
                        return OperatingResult.builder().isContinue(true).data(results).build();
                    }catch (Exception var1){
                        log.warn("缓存用户的查询角色结果错误，用户id为：{}",user.getId());
                        return OperatingResult.builder().isContinue(true).data(results).build();
                    }
                }
            }
        }
        return OperatingResult.builder().isContinue(true).build();
    }
}
