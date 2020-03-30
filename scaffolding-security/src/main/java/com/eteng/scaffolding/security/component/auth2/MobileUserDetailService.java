package com.eteng.scaffolding.security.component.auth2;

import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.security.service.DelegationUserDetailService;
import com.eteng.scaffolding.admin.service.bo.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 通过手机号的查询用户信息服务
 * 该服务是由Spring Security调用
 * @FileName MobileUserDetailService
 * @Author eTeng
 * @Date 2020/1/8 17:13
 * @Description
 */
@Slf4j
@Service("mobileUserDetailService")
public class MobileUserDetailService extends CustomerUserDetailService {

    @Autowired
    private DelegationUserDetailService delegationUserDetailService;
    
    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

        // TODO: 2020/1/8 验证手机的验证码

        UmsUserDTO umsUserDTO = delegationUserDetailService.loadByPhone(phone);
        log.info("加载用户认证信息，通过手机号码认证。手机号为：{}",phone);
        return Optional.ofNullable(umsUserDTO).map(u -> new SecurityUser(
                u.getId(),
                u.getUserName(),
                u.getCredentials(),
                u.getEnabled(),
                u.getAccountNonExpired(),
                u.getCredentialsNonExpired(),
                u.getAccountNonLocked(),
                super.obtainGrantedAuthorities(u))).orElse(null);
    }
}
