package com.eteng.scaffolding.component.handler;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.common.enums.ResultCode;
import com.eteng.scaffolding.common.exception.ServiceException;
import com.eteng.scaffolding.security.component.exception.OAuthExceptionHanbdler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;

/**
 * @FileName WebGlobalExceptionHandler.java
 * @Author eTeng
 * @Date 2019/12/13 16:49
 * @Description 接口层的全局异常处理器
 */
@Slf4j
@Component
public class WebGlobalExceptionHandler implements HandlerExceptionResolver {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ControllerGlobalExceptionHandler controllerGlobalExceptionHandler;

    @Autowired
    private AuthenticationEntryPoint myAuthenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler myAccessDeniedHandler;

    @Autowired
    private OAuthExceptionHanbdler oAuthExceptionHanbdler;

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        CommonResult failure = null;
        String result = null;
        ModelAndView modelAndView = new ModelAndView();
        PrintWriter print = null;
        try{
            httpServletResponse.setContentType("application/json;charset=utf-8");
            print = httpServletResponse.getWriter();
            if(e instanceof MethodArgumentNotValidException){
                failure = controllerGlobalExceptionHandler.vlidateParams((MethodArgumentNotValidException)e);
            }else if(e instanceof ConstraintViolationException){
                failure = controllerGlobalExceptionHandler.vlidateParams((ConstraintViolationException)e);
            }else if(e instanceof OAuth2Exception){
                OAuth2Exception e1 = (OAuth2Exception) e;
                oAuthExceptionHanbdler.translate(httpServletRequest,httpServletResponse,e1);
                return modelAndView;
            }else if(e instanceof AuthenticationException){
                AuthenticationException e1 = (AuthenticationException) e;
                myAuthenticationEntryPoint.commence(httpServletRequest,httpServletResponse,e1);
                return modelAndView;
            }else if(e instanceof AccessDeniedException){
                AccessDeniedException e1 = (AccessDeniedException) e;
                myAccessDeniedHandler.handle(httpServletRequest,httpServletResponse,e1);
                return modelAndView;
            }else if(e instanceof HttpMessageConversionException){
                failure  = CommonResult.failure(ResultCode.PARAM_ILLEGAL);
            }else if(e instanceof ServiceException){
                failure = CommonResult.failure(e.getMessage());
            }else {
                failure = CommonResult.failure("服务器出错啦,请稍后重试耶！");
            }
            result = objectMapper.writeValueAsString(failure);
            log.error("系统内部发生未知异常，异常信息为：{},url为: {}",e.getStackTrace(),httpServletRequest.getRequestURI());
        }catch (Exception var1){
            log.error("全局异常处理失败，异常信息为：{}",var1.getMessage());
            failure = CommonResult.failure(var1.getMessage());
            try{
                result = objectMapper.writeValueAsString(failure);
            }catch (Exception var2){
                log.error("全局异常处理失败，异常信息为：{}",var2.getMessage());
            }
        }
        this.writer(print,result);
        return modelAndView;
    }

    private void writer(PrintWriter writer,String result){
        if(writer != null){
            writer.write(result);
            writer.flush();
            writer.close();
        }
    }
}
