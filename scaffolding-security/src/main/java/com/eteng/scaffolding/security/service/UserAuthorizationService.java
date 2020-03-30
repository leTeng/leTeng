package com.eteng.scaffolding.security.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 用户授权信息服务
 * @FileName UserAuthorizationService
 * @Author eTeng
 * @Date 2020/1/8 16:16
 * @Description
 */
public interface UserAuthorizationService {

    /**
     * 加载用户的全部权限信息
     * @param userId
     * @return
     */
    Collection<GrantedAuthority> loadAllAuthority(String userId) throws Exception;
}
