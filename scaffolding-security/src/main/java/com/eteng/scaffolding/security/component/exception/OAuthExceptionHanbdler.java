package com.eteng.scaffolding.security.component.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OAuth2 异常处理器
 * @FileName MyOauthExceptionHanbdler
 * @Author eTeng
 * @Date 2020/1/10 16:33
 * @Description
 */
public interface OAuthExceptionHanbdler {

    /**
     * OAuth2Exception 异常处理器
     * @param e
     * @return
     */
    void translate(HttpServletRequest request, HttpServletResponse response, OAuth2Exception e) throws IOException;
}
