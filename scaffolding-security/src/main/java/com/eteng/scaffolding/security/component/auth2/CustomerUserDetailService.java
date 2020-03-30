package com.eteng.scaffolding.security.component.auth2;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.security.service.DelegationUserDetailService;
import com.eteng.scaffolding.security.service.UserAuthorizationService;
import com.eteng.scaffolding.admin.service.bo.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * 自定义的查询用户信息服务
 * 该服务是由Spring Security调用
 * @FileName CustomerUserDetailService
 * @Author eTeng
 * @Date 2020/1/8 15:19
 * @Description
 */
@Slf4j
@Service("userDetailService")
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private DelegationUserDetailService delegationUserDetailService;

    @Autowired
    private UserAuthorizationService userAuthorizationService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UmsUserDTO umsUserDTO = delegationUserDetailService.loadByUsername(userName);
        log.info("加载用户认证信息，通过用户名和密码认证。用户名为：{}",userName);
        return Optional.ofNullable(umsUserDTO).map(u -> new SecurityUser(
                u.getId(),
                u.getUserName(),
                u.getCredentials(),
                u.getEnabled(),
                u.getAccountNonExpired(),
                u.getCredentialsNonExpired(),
                u.getAccountNonLocked(),
                this.obtainGrantedAuthorities(u))).orElse(null);
    }

    public Collection<GrantedAuthority> obtainGrantedAuthorities(UmsUserDTO umsUserDTO){
        Collection<GrantedAuthority> grantedAuthorities = CollUtil.newHashSet();
        try{
            grantedAuthorities.addAll(userAuthorizationService.loadAllAuthority(umsUserDTO.getId()));
            log.info("加载用户权限信息，通过用户名id加载。用户id为：{}",umsUserDTO.getId());
        }catch (Exception var1){
            log.info("加载用户权限信息，出现异常。用户id为：{},异常信息为:{}",umsUserDTO.getId(),var1.getMessage());
            return grantedAuthorities;
        }
        return grantedAuthorities;
    }
}
