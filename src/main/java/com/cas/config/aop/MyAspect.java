package com.cas.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 22:20 2020-02-14
 * @version: V1.0
 * @review: 自定义AOP切点
 */
@Slf4j
@Aspect
@Component
public class MyAspect {

    @Pointcut("execution(* com.cas.controller.*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(lg)")
    public Object around(ProceedingJoinPoint jp, LogHistory lg) throws Throwable {//plain old java object
        Object obj = null;
        if (jp.getArgs().length >= 1) {
            obj = jp.getArgs()[0];
        }
        if (lg.isOpen()) {
            log.info("包名【{}】, 方法名【{}】,参数【{}】", jp.getSignature().getDeclaringTypeName(), jp.getSignature().getName(), obj);
            return jp.proceed();
        }
        return jp.proceed();
    }

}
