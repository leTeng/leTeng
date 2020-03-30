package com.eteng.scaffolding.common.enums;

import com.eteng.scaffolding.common.dto.IErrorCode;

/**
 * @FileName ResultCode.java
 * @Author eTeng
 * @Date 2019/12/12 20:14
 * @Description
 */
public enum  ResultCode implements IErrorCode {

    /**
     * 基本状态码
     */
    SUCCESS(200,"操作成功","Some Data"),
    FAILURE(500,"操作失败","Some Data"),
    VERIFICATION_FAILED(404,"参数校验失败","Some Data"),
    UNAUTHORIZED(401,"未认证","Some Data"),
    FORBIDDEN(403, "无操作权限","Some Data"),

    /**
     * 认证异常状态码
     */
    BADUNAUTHORIZED(10000,"认证失败","Some Data"),
    NOTUSER(10001,"未找到该用户","Some Data"),
    UNAPPROVEDCLIENT(10002,"客户端未授权","Some Data"),
    EXPIREDUSER(10003,"用户已过期","Some Data"),
    LOCKEDUSER(10004,"用户已锁定","Some Data"),
    CREDENTIALSEXPIRED(10005,"密码已过期","Some Data"),
    BADCREDENTIALS(10006,"密码错误","Some Data"),
    NOTCREDENTIALS(10007,"密码为空","Some Data"),
    ACCOUNTEXPIRED(10008,"账号已过期","Some Data"),
    UNSUPPORTEDGRANT(10009,"不支持的授权类型","Some Data"),
    INVALIDSCOPE(10010,"无效的scope","Some Data"),
    INVALIDTOKEN(10011,"无效的token","Some Data"),
    INVALIDCLIENT(10012,"无效的客户端","Some Data"),
    BADCLIENTCREDENTIALS(10013,"客户端秘钥错误","Some Data"),

    /**
     * 客户端异常
     */
    PARAM_ILLEGAL(20001,"请求参数格式非法","Some Data");

    /**
     * 状态码
     */
    private long code;
    /**
     * 消息
     */
    private String message;

    /**
     * url
     */
    private String url;

    ResultCode(long code, String message, String url) {
        this.code = code;
        this.message = message;
        this.url = url;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
