package com.cas.aspect;

import com.cas.enums.CacheEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/16 3:44 下午
 * @desc
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QueryCache {

    /**
     * 指定缓存类型
     * @return
     */
    CacheEnum value();

}
