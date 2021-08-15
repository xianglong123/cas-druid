package com.cas.config.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 22:20 2020-02-14
 * @version: V1.0
 * @review: 自定义AOP切点
 */
@Aspect
@Component
public class MyAspect {
    private static final Logger log = LoggerFactory.getLogger(MyAspect.class);
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
