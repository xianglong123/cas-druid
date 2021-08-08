package com.cas.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
  
/** 
 * mybatis 注解版  
 * 
 */  
@Configuration  
public class MybatisConfig {  
  
    @Bean  
    public ConfigurationCustomizer configurationCustomizer() {  
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true);//设置驼峰命名规则
        };
    }  
}  