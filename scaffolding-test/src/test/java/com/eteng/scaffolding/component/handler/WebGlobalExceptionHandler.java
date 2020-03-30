package com.eteng.scaffolding.component.handler;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        CommonResult failure;
        String result = null;
        ModelAndView modelAndView = new ModelAndView();
        PrintWriter print = null;
        try{
            httpServletResponse.setContentType("application/json;charset=utf-8");
            print = httpServletResponse.getWriter();
            if(e instanceof NullPointerException){
                failure = CommonResult.failure("发生了空指针");
            }else if(e instanceof IllegalArgumentException){
                failure = CommonResult.failure("无效的参数异常");
            }else if(e instanceof MethodArgumentNotValidException){
                failure = controllerGlobalExceptionHandler.vlidateParams((MethodArgumentNotValidException)e);
            }else if(e instanceof ConstraintViolationException){
                failure = controllerGlobalExceptionHandler.vlidateParams((ConstraintViolationException)e);
            }else{
                failure = CommonResult.failure(e.getMessage());
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
