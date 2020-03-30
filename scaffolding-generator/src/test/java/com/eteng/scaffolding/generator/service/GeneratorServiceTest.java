package com.eteng.scaffolding.generator.service;

import com.eteng.scaffolding.test.Junit5Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 代码生成测试
 */
public class GeneratorServiceTest extends Junit5Test{

    @Autowired
    private GeneratorService generatorService;

    @Test
    public void getTables() {
    }

    @Test
    public void getColumns() {
    }

    @Test
    public void generator() {
    }

    /**
     * 代码生成服务测试
     */
    @Test
    public void testGenerator() {
        generatorService.generator();
    }
}