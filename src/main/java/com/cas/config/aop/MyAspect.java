package com.cas.config.aop;

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
@Aspect
@Component
public class MyAspect {

    @Pointcut("execution(* com.cas.controller.*.*(..))")
    public void pointCut() {
    }

    @Around("pointCut() && @annotation(log)")
    public Object around(ProceedingJoinPoint jp, LogHistory log) throws Throwable {//plain old java object
        if (log.isOpen()) {
            System.out.println("环绕被调用");
            return jp.proceed();
        }
        return jp.proceed();
    }

}
