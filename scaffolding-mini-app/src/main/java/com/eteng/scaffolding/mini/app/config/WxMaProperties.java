package com.eteng.scaffolding.mini.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @FileName WxMaProperties.java
 * @Author eTeng
 * @Date 2019/12/12 14:53
 * @Description
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    public List<Config> configs;

    @Data
    public static class Config{

        private String appid;

        private String secret;

        private String token;

        private String aesKey;

        private String msgDataFormat;
    }
}
