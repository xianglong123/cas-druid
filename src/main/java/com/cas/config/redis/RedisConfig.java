package com.cas.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 19:33 2020-02-16
 * @version: V1.0
 * @review: 依赖注入 RedisTemplate 暂时失效
 * springboot 2.0.X 之后redis集成底层用的 Lettuce
 */
@Configuration
public class RedisConfig {


    /**
     * 依赖注入 RedisTemplate
     * @return
     */
    @Bean(name = "cacheRedisTemplate")
    public RedisTemplate<String, Object> initRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        /**
         * 这里重新定义了编码序列化配置，可以支持对象的存储
         */
        redisTemplate.setDefaultSerializer(new ObjectRedisSerializer());
        redisTemplate.setKeySerializer(new ObjectRedisSerializer());
        redisTemplate.setValueSerializer(new ObjectRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }


}
