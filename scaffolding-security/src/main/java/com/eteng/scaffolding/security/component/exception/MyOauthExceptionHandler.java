package com.eteng.scaffolding.security.component.exception;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.common.enums.ResultCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @FileName DefaultMyOauthExceptionHandler
 * @Author eTeng
 * @Date 2020/1/10 16:41
 * @Description
 */
@Slf4j
@Component
public class MyOauthExceptionHandler implements OAuthExceptionHanbdler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void translate(HttpServletRequest request, HttpServletResponse response, OAuth2Exception e) throws IOException{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        CommonResult result;
        if (e instanceof InvalidGrantException) {
            result = CommonResult.failure(ResultCode.BADUNAUTHORIZED);
        }else if(e instanceof UnsupportedGrantTypeException){
            result = CommonResult.failure(ResultCode.UNSUPPORTEDGRANT);
        }else if(e instanceof InvalidScopeException){
            result = CommonResult.failure(ResultCode.INVALIDSCOPE);
        }else if(e instanceof InvalidTokenException){
            result = CommonResult.failure(ResultCode.INVALIDTOKEN);
        }else if(e instanceof InvalidClientException){
            result = CommonResult.failure(ResultCode.INVALIDCLIENT);
        }else if(e instanceof BadClientCredentialsException){
            result = CommonResult.failure(ResultCode.BADCLIENTCREDENTIALS);
        }else{
            result = CommonResult.failure(ResultCode.BADUNAUTHORIZED);
        }
        this.printf(writer,result);
    }

    private void printf(PrintWriter printWriter, CommonResult result)throws IOException {
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
