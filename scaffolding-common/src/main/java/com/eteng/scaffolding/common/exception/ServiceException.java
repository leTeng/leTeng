package com.eteng.scaffolding.common.exception;

/**
 * 业务异常
 * @FileName ServiceException
 * @Author eTeng
 * @Date 2020/3/24 17:29
 * @Description
 */
public class ServiceException extends RuntimeException{
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
