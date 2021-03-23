package com.cas.config.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午8:27 2021/3/22
 * @version: V1.0
 * @review:
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogHistory {

    boolean isOpen() default true;

}
