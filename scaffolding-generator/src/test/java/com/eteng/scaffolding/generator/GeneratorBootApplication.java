package com.eteng.scaffolding.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class GeneratorBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeneratorBootApplication.class, args);
    }

}