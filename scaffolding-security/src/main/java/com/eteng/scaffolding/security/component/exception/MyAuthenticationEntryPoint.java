package com.eteng.scaffolding.security.component.exception;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.common.enums.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 身份验证异常处理器
 * @FileName MyAuthenticationEntryPoint
 * @Author eTeng
 * @Date 2020/1/10 11:56
 * @Description
 */
@Slf4j
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        CommonResult result;
        if (authException instanceof UsernameNotFoundException) {
            result = CommonResult.failure(ResultCode.NOTUSER);
        }else if(authException instanceof UnapprovedClientAuthenticationException){
            result = CommonResult.failure(ResultCode.UNAPPROVEDCLIENT);
        }else if(authException instanceof NonceExpiredException){
            result = CommonResult.failure(ResultCode.EXPIREDUSER);
        }else if(authException instanceof LockedException){
            result = CommonResult.failure(ResultCode.LOCKEDUSER);
        }else if(authException instanceof CredentialsExpiredException){
            result = CommonResult.failure(ResultCode.CREDENTIALSEXPIRED);
        }else if(authException instanceof BadCredentialsException){
            result = CommonResult.failure(ResultCode.BADCREDENTIALS);
        }else if(authException instanceof AuthenticationCredentialsNotFoundException){
            result = CommonResult.failure(ResultCode.NOTCREDENTIALS);
        }else if(authException instanceof AccountExpiredException){
            result = CommonResult.failure(ResultCode.ACCOUNTEXPIRED);
        }else if(authException instanceof InsufficientAuthenticationException){
            result = CommonResult.failure(ResultCode.UNAUTHORIZED);
        } else{
            result = CommonResult.failure(ResultCode.BADUNAUTHORIZED);
        }
        this.printf(writer,result);
    }

    private void printf(PrintWriter printWriter,CommonResult result)throws IOException{
        String resultStr;
        try{
            resultStr = objectMapper.writeValueAsString(result);
        }catch (IOException var1){
            log.error("用户认证异常信息转换时发生异常，异常信息为:{}",var1.getMessage());
            throw var1;
        }
        printWriter.println(resultStr);
    }
}
