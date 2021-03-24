package com.cas.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis配置
 */
@Slf4j
@Configuration
public class MybatisConfiguration {

    /**
     * 注册拦截器，这样生成sqlSessionFactory的时候就会找到这个拦截器并配置进去
     */
    @Bean
    public MybatisInterceptor mybatisInterceptor() {
        MybatisInterceptor interceptor = new MybatisInterceptor();
        Properties properties = new Properties();
        // 可以调用properties.setProperty方法来给拦截器设置一些自定义参数
        interceptor.setProperties(properties);
        return interceptor;
    }


}
