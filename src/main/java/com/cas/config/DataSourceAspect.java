package com.cas.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @program: springboot
 * @description: 动态切换数据源AOP切面处理
 * @author: gourd
 * @date: 2018-11-22 14:03
 * @since: 1.0
 **/
@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

    /**
     * 切点: 所有配置 TargetDataSource 注解的方法
     */
    @Pointcut("@annotation(com.cas.config.TargetDataSource)")
    public void dataSourcePointCut() {}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        TargetDataSource ds = method.getAnnotation(TargetDataSource.class);
        // 通过判断 @ChangeDataSource注解 中的值来判断当前方法应用哪个数据源
        DynamicDataSource.setDataSource(ds.value());
        log.info("^o^= 当前数据源: " + ds.value());
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("^o^= clean datasource");
        }
    }
    
    @Override
    public int getOrder() {
        return 1;
    }
}
