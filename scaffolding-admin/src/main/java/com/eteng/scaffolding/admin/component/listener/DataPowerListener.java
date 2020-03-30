package com.eteng.scaffolding.admin.component.listener;

import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.common.annotation.DynamicPower;

/**
 * @FileName DataPowerListener
 * @Author eTeng
 * @Date 2020/1/8 16:06
 * @Description
 */
public interface DataPowerListener {

    /**
     * 缓存前缀
     */
    String CACHE_PREFIX = "POWER:";
    /**
     * 缓存过期
     */
    long TIMEOUT = 120 * 60;

    /**
     * 是否匹配的权限类型
     * @param powerType
     * @return
     */
    boolean match(String powerType);

    /**
     * 操作时机
     * @return
     */
    OperatingOpportunity opportunity();

    /**
     * 操作权限
     * @param opr 操作动作
     * @param operatingData 操作的数据
     */
    OperatingResult operatingPower(DynamicPower.OperatorType opr, Object... operatingData) throws Exception;
}
