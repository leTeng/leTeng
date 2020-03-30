package com.eteng.scaffolding.admin.component.aspect;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingOpportunity;
import com.eteng.scaffolding.admin.component.aspect.bean.OperatingResult;
import com.eteng.scaffolding.admin.component.listener.DataPowerListener;
import com.eteng.scaffolding.common.annotation.DynamicPower;
import com.eteng.scaffolding.common.annotation.DynamicPowers;
import com.eteng.scaffolding.common.util.AspectUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 数据权限切面
 * @FileName DataPowerAspect
 * @Author eTeng
 * @Date 2020/1/2 9:44
 * @Description
 */
@Component
@Aspect
public class DataPowerAspect {

    private Set<DataPowerListener> dataPowerListeners;

    public Set<DataPowerListener> getDataPowerListeners() {
        return dataPowerListeners;
    }
    @Autowired(required = false)
    public void setDataPowerListeners(Set<DataPowerListener> dataPowerListeners) {
        this.dataPowerListeners = dataPowerListeners;
    }

    @Pointcut("execution(public * com.eteng.scaffolding.admin..*.*(..)) && args(..) && @annotation(com.eteng.scaffolding.common.annotation.DynamicPower)")
    public void dataPowerPoint(){}

    @Pointcut("execution(public * com.eteng.scaffolding.admin..*.*(..)) && args(..) && @annotation(com.eteng.scaffolding.common.annotation.DynamicPowers)")
    public void combinationDataPowerPoint(){}

    @Around("dataPowerPoint()")
    public Object around(ProceedingJoinPoint joinPoint)throws Throwable{
        DynamicPower dynamicPower = AspectUtil.getMethodAnno(joinPoint, DynamicPower.class);
        return procees(joinPoint,dynamicPower);
    }

    @Around("combinationDataPowerPoint()")
    public Object combinationAround(ProceedingJoinPoint joinPoint)throws Throwable{
        DynamicPowers dynamicPowers = AspectUtil.getMethodAnno(joinPoint, DynamicPowers.class);
        DynamicPower[] dynamicPowerList = dynamicPowers.dynamicPower();
        if(ArrayUtil.isNotEmpty(dynamicPowerList)){
            for (DynamicPower dynamicPower : dynamicPowerList) {
                return procees(joinPoint,dynamicPower);
            }
        }
        return null;
    }

    private Object procees(ProceedingJoinPoint joinPoint,DynamicPower dynamicPower)throws Throwable{
        String powerType = null;
        DynamicPower.OperatorType opr = null;
        if(dynamicPower != null){
            opr = dynamicPower.opr();
            powerType = dynamicPower.powerType();
            if (CollUtil.isNotEmpty(dataPowerListeners)) {
                for (DataPowerListener listener : dataPowerListeners) {
                    if(!listener.match(powerType) || !listener.opportunity().equals(OperatingOpportunity.BEFORE)){
                        continue;
                    }
                    OperatingResult result = listener.operatingPower(opr, AspectUtil.getTargetMethodArgs(joinPoint));
                    if(result != null && !result.isContinue()){
                        return result.getData();
                    }
                }
            }
        }
        Object proceed = joinPoint.proceed();
        if(dynamicPower != null){
            if (CollUtil.isNotEmpty(dataPowerListeners)) {
                for (DataPowerListener listener : dataPowerListeners) {
                    if(!listener.match(powerType) || !listener.opportunity().equals(OperatingOpportunity.AFTER)){
                        continue;
                    }
                    OperatingResult result = listener.operatingPower(opr, AspectUtil.getTargetMethodArgs(joinPoint),proceed);
                    if(result != null && !result.isContinue()){
                        return proceed;
                    }
                }
            }
        }
        return proceed;
    }
}
