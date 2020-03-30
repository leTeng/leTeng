package com.eteng.scaffolding.security.service.impl;

import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.security.service.UserAuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @FileName UserAuthorizationServiceImpl
 * @Author eTeng
 * @Date 2020/1/8 16:17
 * @Description
 */
@Slf4j
@Service
public class UserAuthorizationServiceImpl implements UserAuthorizationService {

    @Autowired
    private UmsUserService umsUserService;

    @Override
    public Collection<GrantedAuthority> loadAllAuthority(String userId) throws Exception{
        Collection<String> authoritys = umsUserService.loadAuthority(userId);
        return authoritys.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toSet());
    }
}
