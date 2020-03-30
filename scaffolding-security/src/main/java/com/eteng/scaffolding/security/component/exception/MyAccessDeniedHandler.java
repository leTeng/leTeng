package com.eteng.scaffolding.security.component.exception;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @FileName MyAccessDeniedHandler
 * @Author eTeng
 * @Date 2020/1/10 12:59
 * @Description
 */
@Slf4j
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        String resultStr;
        PrintWriter printWriter;
        try{
            printWriter = response.getWriter();
            CommonResult result = CommonResult.forbidden();
            resultStr = objectMapper.writeValueAsString(result);
        }catch (IOException var1){
            log.error("用户认证异常信息转换时发生异常，异常信息为:{}",var1.getMessage());
            throw var1;
        }
        printWriter.println(resultStr);
    }
}
