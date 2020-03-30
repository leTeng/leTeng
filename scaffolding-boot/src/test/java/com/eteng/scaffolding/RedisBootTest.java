package com.eteng.scaffolding;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 应用启动redis连接测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBootTest {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String BASE_KEY = "scaffolding:test";

    /**
     * 测试设置 val
     */
    @Test
    public void setValTest(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set(BASE_KEY,"test");

        String result = (String) valueOperations.get(BASE_KEY);
        Assert.assertEquals("test",result);
    }


    /**
     * 测试删除 val
     */
    @Test
    public void delValTest(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        redisTemplate.delete(BASE_KEY);

        String result = (String) valueOperations.get(BASE_KEY);
        Assert.assertNull(result);
    }
}