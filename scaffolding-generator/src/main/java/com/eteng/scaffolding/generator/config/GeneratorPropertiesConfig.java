package com.eteng.scaffolding.generator.config;

import com.eteng.scaffolding.generator.domain.GenConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @FileName GeneratorPropertiesConfig
 * @Author eTeng
 * @Date 2019/12/18 14:25
 * @Description
 */
@Component
@ConfigurationProperties(prefix = "generator.auto")
@Data
public class GeneratorPropertiesConfig {

    List<AdminGeneratorConfig> configs;

    @Data
    public static class AdminGeneratorConfig{
        /**
         * 生成的配置
         */
        private GenConfig genConfig;

        /**
         * 指定的表对应类的名称
         */
        private List<String> tables;
    }
}
