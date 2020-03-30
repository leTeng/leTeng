package com.eteng.scaffolding.common.dto;

/**
 * @FileName IErrorCode.java
 * @Author eTeng
 * @Date 2019/12/12 20:34
 * @Description
 */
public interface IErrorCode {

    /**
     * 获取错误码
     * @return
     */
    long getCode();

    /**
     * 获取错误信息
     * @return
     */
    String getMessage();

    /**
     * 获取url
     */
    String getUrl();
}
