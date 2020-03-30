package com.eteng.scaffolding.mini.app.pojo.dto;

import lombok.Builder;

/**
 * @FileName LoginVo.java
 * @Author eTeng
 * @Date 2019/12/12 20:58
 * @Description
 */
@Builder
public class LoginResult {

    /**
     * token
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
