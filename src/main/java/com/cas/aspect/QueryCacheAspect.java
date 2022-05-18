package com.cas.aspect;

import cn.hutool.core.util.ReflectUtil;
import com.cas.bean.BigData;
import com.cas.enums.CacheEnum;
import com.cas.enums.Constants;
import com.cas.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 22:20 2020-02-14
 * @version: V1.0
 * @review: 从缓存中获取数据
 */
@Aspect
@Component
public class QueryCacheAspect {

    private static final Logger log = LoggerFactory.getLogger(QueryCacheAspect.class);

    @Resource
    private RedisUtil redisUtil;

    @Pointcut("@annotation(com.cas.aspect.QueryCache)")
    private void controllerAspect(){}

    /**
     * 查询缓存
     * @param pj
     * @param queryCache
     */
    @Around(value = "controllerAspect()&&@annotation(queryCache)", argNames = "pj,queryCache")
    public Object around(ProceedingJoinPoint pj, QueryCache queryCache) throws Throwable {
        CacheEnum cacheEnum = queryCache.value();
        String cacheKey = String.valueOf(ReflectUtil.getFieldValue(pj.getArgs()[0], cacheEnum.getCacheKey()));
        String key = cacheEnum.getPrefix() + cacheKey;
        Object cacheObj = redisUtil.get(key);
        return cacheObj != null ? cacheObj : cacheQuery(pj, key);
    }

    private Object cacheQuery(ProceedingJoinPoint pj, String key) throws Throwable {
        System.out.println("数据缓存");
        Object proceed = pj.proceed();
        redisUtil.setEx(key, proceed, 30L);
        return proceed;
    }


}
