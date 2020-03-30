package com.eteng.scaffolding.mini.app.compoent;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.eteng.scaffolding.mini.app.config.WxMaProperties;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @FileName WxMaServiceManager.java
 * @Author eTeng
 * @Date 2019/12/12 19:32
 * @Description
 */
@Component
@Slf4j
public class WxMaServiceManager {

    private Map<String,WxMaService> wxMaServices = Maps.newHashMap();
    private WxMaProperties properties;

    public WxMaProperties getProperties() {
        return properties;
    }

    @Autowired
    public void setProperties(WxMaProperties properties) {
        this.properties = properties;
    }

    public WxMaService getMaService(String appid) {
        WxMaService wxService = wxMaServices.get(appid);
        if (wxService == null) {
            log.error(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
            throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appid));
        }

        return wxService;
    }

    @PostConstruct
    public void init(){
        List<WxMaProperties.Config> configs = this.properties.configs;
        if (configs == null) {
            log.error("小程序配置为空，请检查配置！");
            throw new RuntimeException("小程序配置为空，请检查配置！");
        }
        wxMaServices = configs.stream().map(config -> {
            String appid = config.getAppid();
            String secret = config.getSecret();
            String token = config.getToken();
            String aesKey = config.getAesKey();
            String msgDataFormat = config.getMsgDataFormat();
            WxMaDefaultConfigImpl defaultConfig = new WxMaDefaultConfigImpl();
            defaultConfig.setAppid(appid);
            defaultConfig.setSecret(secret);
            defaultConfig.setToken(token);
            defaultConfig.setAesKey(aesKey);
            defaultConfig.setMsgDataFormat(msgDataFormat);
            WxMaService wxMaServiceImpl = new WxMaServiceImpl();
            wxMaServiceImpl.setWxMaConfig(defaultConfig);
            log.info("加载小程序配置成功, appid:{} , secret:{} , token:{} ," +
                    " aesKey:{} , msgDataFormat:{}",appid,secret,token,aesKey,msgDataFormat);
            return wxMaServiceImpl;
        }).collect(Collectors.toMap(s -> s.getWxMaConfig().getAppid(), a -> a,(s, a) -> s));
    }
}
