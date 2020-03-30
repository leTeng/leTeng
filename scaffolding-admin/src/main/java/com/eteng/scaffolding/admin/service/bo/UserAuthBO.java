package com.eteng.scaffolding.admin.service.bo;

import lombok.Data;

import java.util.Collection;

/**
 * 用户认证后存储信息
 * @FileName UserAuthBO
 * @Author eTeng
 * @Date 2020/1/3 11:00
 * @Description
 */
@Data
public class UserAuthBO {

    /**
     * id
     */
    private String id;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 权限
     */
    private Collection<String> permission;
}
