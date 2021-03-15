package com.cas.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:01 2021/3/15
 * @version: V1.0
 * @review:
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {
    String value() default CommonConstant.MASTER_DATASOURCE;
    //代码切换到数据源
//    DynamicDataSource.setDataSource(CommonConstant.MASTER_DATASOURCE);
//    DynamicDataSource.clearDataSource();
}
