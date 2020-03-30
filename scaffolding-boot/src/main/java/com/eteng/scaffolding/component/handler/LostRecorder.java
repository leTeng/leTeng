package com.eteng.scaffolding.component.handler;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 丢弃的消息记录器
 * @FileName LostRecorder
 * @Author eTeng
 * @Date 2020/3/25 10:43
 * @Description
 */
@Component
public class LostRecorder {

    private final static String LOST_MESSAGE_KEY = "LOST_MESSAGE:";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 记录
     * @param id
     * @return
     */
    public void record(String id){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(LOST_MESSAGE_KEY + id,"T");
        // 缓存一个小时
        redisTemplate.expire(LOST_MESSAGE_KEY + id,1, TimeUnit.HOURS);
    }

    /**
     * 是否记录
     * @return
     */
    public boolean isRecord(String id){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String val = operations.get(LOST_MESSAGE_KEY + id);
        return StrUtil.isNotBlank(val);
    }

    /**
     * 清除记录
     * @param id
     */
    public void clean(String id){
        redisTemplate.delete(LOST_MESSAGE_KEY + id);
    }
}
