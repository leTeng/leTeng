package com.eteng.scaffolding.security.service;

import com.eteng.scaffolding.admin.service.dto.UmsUserDTO;

/**
 * 用户详细信息委托服务
 * @FileName DelegationUserDetailService
 * @Author eTeng
 * @Date 2020/1/8 16:26
 * @Description
 */
public interface DelegationUserDetailService {

    UmsUserDTO loadByUsername(String username);

    UmsUserDTO loadByPhone(String phone);
}
