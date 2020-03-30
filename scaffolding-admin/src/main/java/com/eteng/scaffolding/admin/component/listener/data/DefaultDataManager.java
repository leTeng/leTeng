package com.eteng.scaffolding.admin.component.listener.data;

import cn.hutool.core.collection.CollUtil;
import com.eteng.scaffolding.admin.service.dto.UmsMenuDTO;
import com.eteng.scaffolding.admin.service.dto.UmsResourceDTO;
import com.eteng.scaffolding.admin.service.dto.UmsRoleDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 默认的临时权限数据管理器
 * @FileName DefaultDataManager
 * @Author eTeng
 * @Date 2020/1/5 14:52
 * @Description
 */
@Component
public class DefaultDataManager implements DataManager{

    /**
     * 空数据标识
     */
    public static final String EMPTRY = "empty";

    /**
     * 数据过期时间
     */
    private long expire = 60 * 120;
    private final static String BASE_PREFIX = "power:";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Collection<UmsRoleDTO> getRole(String dataType, String key) throws Exception {
        ListOperations<String, String> ops = getOps();
        key = this.generatorKey(dataType, key);
        List<String> result = ops.range(key, 0, -1);
        if(CollUtil.isEmpty(result)){
            return null;
        }
        if(result.size() == 1 && result.get(0).equals(EMPTRY)){
            return CollUtil.newArrayList();
        }
        return result.stream().map(r -> {
            try{
                return objectMapper.readValue(r, UmsRoleDTO.class);
            }catch (Exception var1){
                throw new RuntimeException(var1.getMessage());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean putRole(String dataType, String key, UmsRoleDTO... value) throws Exception {
        ListOperations<String, String> ops = getOps();
        removeRole(dataType, key);
        key = this.generatorKey(dataType, key);
        List<String> values = CollUtil.newArrayList();
        if(value == null || value.length == 0){
            values.add(EMPTRY);
        }else{
            for (UmsRoleDTO roleDTO : value) {
                values.add(objectMapper.writeValueAsString(roleDTO));
            }
        }
        Long aLong = ops.rightPushAll(key, values);
        redisTemplate.expire(key,expire, TimeUnit.SECONDS);
        return aLong != null && aLong != 0;
    }

    @Override
    public boolean removeRole(String dataType, String... keys) throws Exception {
        Collection<String> lastKeys = CollUtil.newArrayList();
        for (String s : keys) {
            lastKeys.add(this.generatorKey(dataType,s));
        }
        Long delete = redisTemplate.delete(lastKeys);
        return delete != null && delete != 0;
    }

    @Override
    public Collection<UmsMenuDTO> getMenu(String dataType, String key) throws Exception {
        ListOperations<String, String> ops = getOps();
        key = this.generatorKey(dataType, key);
        List<String> result = ops.range(key, 0, -1);
        if(CollUtil.isEmpty(result)){
            return null;
        }
        if(result.size() == 1 && result.get(0).equals(EMPTRY)){
            return CollUtil.newArrayList();
        }
        return result.stream().map(r -> {
            try{
                return objectMapper.readValue(r, UmsMenuDTO.class);
            }catch (Exception var1){
                throw new RuntimeException(var1.getMessage());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean putMenu(String dataType, String key, UmsMenuDTO... value) throws Exception {
        ListOperations<String, String> ops = getOps();
        removeRole(dataType, key);
        key = this.generatorKey(dataType, key);
        List<String> values = CollUtil.newArrayList();
        if(value == null || value.length == 0){
            values.add(EMPTRY);
        }else{
            for (UmsMenuDTO menuDTO : value) {
                values.add(objectMapper.writeValueAsString(menuDTO));
            }
        }
        Long aLong = ops.rightPushAll(key, values);
        redisTemplate.expire(key,expire, TimeUnit.SECONDS);
        return aLong != null && aLong != 0;
    }

    @Override
    public boolean removeMenu(String dataType, String... keys) throws Exception {
        Collection<String> lastKeys = CollUtil.newArrayList();
        for (String s : keys) {
            lastKeys.add(this.generatorKey(dataType,s));
        }
        Long delete = redisTemplate.delete(lastKeys);
        return delete != null && delete != 0;
    }

    @Override
    public Collection<UmsResourceDTO> getResource(String dataType, String key) throws Exception {
        ListOperations<String, String> ops = getOps();
        key = this.generatorKey(dataType, key);
        List<String> result = ops.range(key, 0, -1);
        if(CollUtil.isEmpty(result)){
            return null;
        }
        if(result.size() == 1 && result.get(0).equals(EMPTRY)){
            return CollUtil.newArrayList();
        }
        return result.stream().map(r -> {
            try{
                return objectMapper.readValue(r, UmsResourceDTO.class);
            }catch (Exception var1){
                throw new RuntimeException(var1.getMessage());
            }
        }).collect(Collectors.toList());
    }

    @Override
    public boolean putResource(String dataType, String key, UmsResourceDTO... value) throws Exception {
        ListOperations<String, String> ops = getOps();
        removeRole(dataType, key);
        key = this.generatorKey(dataType, key);
        List<String> values = CollUtil.newArrayList();
        if(value == null || value.length == 0){
            values.add(EMPTRY);
        }else{
            for (UmsResourceDTO resourceDTO : value) {
                values.add(objectMapper.writeValueAsString(resourceDTO));
            }
        }
        Long aLong = ops.rightPushAll(key, values);
        redisTemplate.expire(key,expire, TimeUnit.SECONDS);
        return aLong != null && aLong != 0;
    }

    @Override
    public boolean removeResource(String dataType, String... keys) throws Exception {
        Collection<String> lastKeys = CollUtil.newArrayList();
        for (String s : keys) {
            lastKeys.add(this.generatorKey(dataType,s));
        }
        Long delete = redisTemplate.delete(lastKeys);
        return delete != null && delete != 0;
    }

    private ListOperations<String, String> getOps(){
        return redisTemplate.opsForList();
    }

    private String generatorKey(String powerType,String key){
        return BASE_PREFIX + powerType + key;
    }

}
