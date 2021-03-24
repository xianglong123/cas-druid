package com.cas.config.factory;

import java.util.List;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xianglong
 */
@Slf4j
@Configuration
@Component
public class MyObjectFactory extends DefaultObjectFactory {

    private static final long serialVersionUID = -4293520460481008255L;


    private Object temp = null;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        log.info("初始化参数：【" + properties.toString() + "】");
    }

    /**
     * 方法2
     */
    @Override
    public <T> T create(Class<T> type) {
        T result = super.create(type);
        log.info("创建对象：" + result.toString());
        log.info("是否和上次创建的是同一个对象：【" + (temp == result) + "】");
        return result;
    }

    /**
     * 方法1
     */
    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes,
            List<Object> constructorArgs) {
        T result = super.create(type, constructorArgTypes, constructorArgs);
        log.info("创建对象：" + result.toString());
        temp = result;
        return result;
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return super.isCollection(type);
    }
}
