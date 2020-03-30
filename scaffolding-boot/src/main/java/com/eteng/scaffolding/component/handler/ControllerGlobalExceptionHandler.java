package com.eteng.scaffolding.component.handler;

import cn.hutool.core.util.StrUtil;
import com.eteng.scaffolding.common.dto.CommonResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Iterator;

/**
 * @FileName ControllerGlobalExceptionHandler
 * @Author eTeng
 * @Date 2019/12/23 18:16
 * @Description 控制层异常处理器
 */
@RestControllerAdvice
public class ControllerGlobalExceptionHandler {


    /**
     * 接口参数校验异常处理器
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<String> vlidateParams(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "校验失败:";
        Iterator<FieldError> iterator = bindingResult.getFieldErrors().iterator();
        while (iterator.hasNext()){
            errorMesssage += iterator.next().getDefaultMessage() + ", ";
        }
        errorMesssage = StrUtil.subBefore(errorMesssage, ",", true);
        return CommonResult.validateFailed(errorMesssage);
    }

    /**
     * 类级别的参数校验异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.OK)
    public CommonResult<String> vlidateParams(ConstraintViolationException e){
        return CommonResult.validateFailed(e.getMessage());
    }
}
