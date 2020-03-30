package com.eteng.scaffolding.security.service.impl;

import com.eteng.scaffolding.admin.service.UmsUserService;
import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;
import com.eteng.scaffolding.security.service.DelegationUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @FileName DelegationUserDetailServiceImpl
 * @Author eTeng
 * @Date 2020/1/8 16:27
 * @Description
 */
@Slf4j
@Service
public class DelegationUserDetailServiceImpl implements DelegationUserDetailService {

    @Autowired
    private UmsUserService umsUserService;

    @Override
    public UmsUserDTO loadByUsername(String username) {
        return umsUserService.findByUserName(username);
    }

    @Override
    public UmsUserDTO loadByPhone(String phone) {
        return umsUserService.findByPhone(phone);
    }
}
