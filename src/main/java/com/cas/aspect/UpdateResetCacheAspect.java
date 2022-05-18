package com.cas.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.cas.bean.BigData;
import com.cas.enums.CacheEnum;
import com.cas.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.time.LocalDateTime;

import static com.cas.config.mq.RabbitConfig.MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_EXCHANGE;
import static com.cas.config.mq.RabbitConfig.MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_ROUTING_KEY;


/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 22:20 2020-02-14
 * @version: V1.0
 * @review: 自定义注解重置缓存
 */
@Aspect
@Component
public class UpdateResetCacheAspect {

    private static final Logger log = LoggerFactory.getLogger(UpdateResetCacheAspect.class);

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Pointcut("@annotation(com.cas.aspect.UpdateResetCache)")
    private void controllerAspect(){}

    @Around(value = "controllerAspect()&&@annotation(updateResetCache)", argNames = "pj,updateResetCache")
    public Integer afterReturning(ProceedingJoinPoint pj, UpdateResetCache updateResetCache) throws Throwable {
        CacheEnum cacheEnum = updateResetCache.value();
        String cacheKey = String.valueOf(ReflectUtil.getFieldValue(pj.getArgs()[0], cacheEnum.getCacheKey()));
        // 1、先删除缓存
        String key = cacheEnum.getPrefix() + cacheKey;
        redisUtil.del(key);

        // 2、更新数据
        Integer proceed = (Integer) pj.proceed();

        // 3、延迟删除缓存
        log.info("延迟删除被触发, 当前时间=[{}]", LocalDateTime.now());
        rabbitTemplate.convertAndSend(MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_EXCHANGE, MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_ROUTING_KEY, key);
        return proceed;
    }
}
