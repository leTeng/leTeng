package com.eteng.scaffolding.common.exception;

import cn.hutool.core.collection.CollUtil;
import org.springframework.util.StringUtils;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
public class EntityExistException extends ServiceException {

    public EntityExistException(String message) {
        super(message);
    }

    public EntityExistException(Class clazz, String field, String val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, val));
    }

    public EntityExistException(Class clazz, String field, Object... val) {
        super(EntityExistException.generateMessage(clazz.getSimpleName(), field, CollUtil.newArrayList(val).toString()));
    }

    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " existed";
    }
}