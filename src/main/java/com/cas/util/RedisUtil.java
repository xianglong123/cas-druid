package com.cas.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/17 3:09 下午
 * @desc
 */
@Service
public class RedisUtil {
    
    @Resource
    private RedisTemplate cacheRedisTemplate;


    /**
     * 设置 String 类型 key-value
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        cacheRedisTemplate.opsForValue().set(key, value);
    }

    public void setEx(String key, String value, Long timeout) {
        cacheRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public Boolean setIfAbsent(String key, String value, Long timeout) {
        return cacheRedisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);
    }

    public void setEx(String key, Object value, Long timeout) {
        cacheRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void set(String key, Object value) {
        cacheRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取 String 类型 key-value
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return cacheRedisTemplate.opsForValue().get(key);
    }

    public void del(String key) {
        cacheRedisTemplate.delete(key);
    }




}
