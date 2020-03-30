package com.eteng.scaffolding.security.component.auth2.granter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

/**
 * 手机号认证信息包装Bean
 * @FileName MobileAuthenticationToken
 * @Author eTeng
 * @Date 2020/1/8 17:35
 * @Description
 */
public class MobileAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public MobileAuthenticationToken(Authentication authentication) {
        super(authentication.getPrincipal(), authentication.getCredentials());
    }
}
