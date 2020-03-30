package com.eteng.scaffolding.common.dto;

import com.eteng.scaffolding.common.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @FileName CommonRet.java
 * @Author eTeng
 * @Date 2019/12/12 20:13
 * @Description 接口统一返回结果
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class CommonResult<T> {

    public static final String QUERY_MESSAGE = "查询成功";
    public static final String UPDATE_MESSAGE = "修改成功";
    public static final String ADD_MESSAGE = "添加成功";
    public static final String DEL_MESSAGE = "删除成功";
    public static final String EXPORT_MESSAGE = "导出成功";

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
    private long code;

    /**
     * 消息
     */
    @ApiModelProperty("响应消息")
    private String msg;

    /**
     * 数据
     */
    @ApiModelProperty("返回数据")
    private T data;

    public CommonResult() {
    }

    protected CommonResult(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    protected CommonResult(long code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功操作
     * @param t 返回结果数据
     * @return
     */
    public static <T> CommonResult success(T t){
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),t);
    }

    /**
     * 成功操作
     * @param message 成功信息
     * @param t 返回结果数据
     * @return
     */
    public static <T> CommonResult success(String message,T t){
        return new CommonResult<T>(ResultCode.SUCCESS.getCode(),message,t);
    }

    /**
     * 失败操作
     * @param errorCode 自定义的失败信息和状态码
     * @return
     */
    public static <T> CommonResult failure(IErrorCode errorCode){
        return new CommonResult<T>(errorCode.getCode(),errorCode.getMessage(),null);
    }

    /**
     * 失败操作
     * @param message 自定义的失败信息
     * @return
     */
    public static <T> CommonResult failure(String message){
        return new CommonResult<T>(ResultCode.FAILURE.getCode(),message,null);
    }

    /**
     * 失败操作
     * @return
     */
    public static <T> CommonResult failure(){
        return new CommonResult<T>(ResultCode.FAILURE.getCode(),ResultCode.FAILURE.getMessage(),null);
    }

    /**
     * 参数校验错误
     * @param message 错误信息
     * @return
     */
    public static <T> CommonResult validateFailed(String message) {
        return new CommonResult<T>(ResultCode.VERIFICATION_FAILED.getCode(), message, null);
    }

    /**
     * 参数校验错误
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> validateFailed() {
        return new CommonResult<T>(ResultCode.VERIFICATION_FAILED.getCode(), ResultCode.VERIFICATION_FAILED.getMessage(), null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult unauthorized() {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> CommonResult unauthorized(T data) {
        return new CommonResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult forbidden() {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), null);
    }

    /**
     * 未授权返回结果
     */
    public static <T> CommonResult forbidden(T data) {
        return new CommonResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }
}
