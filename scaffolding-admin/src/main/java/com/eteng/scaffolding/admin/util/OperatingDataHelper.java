package com.eteng.scaffolding.admin.util;

import cn.hutool.core.util.ObjectUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @FileName OperatingDataHelper
 * @Author eTeng
 * @Date 2020/1/4 16:24
 * @Description
 */
public class OperatingDataHelper {

    /**
     * 是否分页查询，如果是将不使用缓存的数据作为查询结果
     * @return
     */
    public static boolean pageable(boolean includeResult,Object... operatingData) {
        Pageable param = param(PageRequest.class, operatingData, includeResult);
        return ObjectUtil.isNotNull(param);
    }

    /**
     * 获取参数
     * @param operatingData 目标方法参数列表
     * @return
     */
    public static <T> T param(Class<T> paramsClz, Object[] operatingData,boolean includeResult){
        T ret = null;
        if (ObjectUtil.isNotNull(operatingData)) {
            Object [] args;
            if(includeResult){
                args = (Object [])operatingData[0];
            }else{
                args = operatingData;
            }
            Object arg;
            int i = 0;
            while (i < args.length){
                arg = args[i];
                if(arg != null && arg.getClass().isAssignableFrom(paramsClz)){
                    return ((T) arg);
                }
                i++;
            }
        }
        return ret;
    }
}
