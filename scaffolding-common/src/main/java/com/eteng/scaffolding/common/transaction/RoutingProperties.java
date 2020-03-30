package com.eteng.scaffolding.common.transaction;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @FileName RoutingProperties
 * @Author eTeng
 * @Date 2020/3/12 23:13
 * @Description
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "transaction.global")
public class RoutingProperties {
    private String exchange;
    private String postQueue;
    private String putQueue;
    private String deleteQueue;
    private Integer maxRetry;
}
