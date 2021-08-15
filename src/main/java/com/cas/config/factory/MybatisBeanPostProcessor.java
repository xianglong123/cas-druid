package com.cas.config.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/10 9:46 下午
 * @desc 这里会对所有容器中的实例进行前置动作
 */
@Component
public class MybatisBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if ("MapperFactoryBean".equals(bean.getClass().getSimpleName())) {
            System.out.println("BeanPostProcessor调用" +
                    "postProcessBeforeInitialization 方法，参数【"
                    + bean.getClass().getSimpleName() + "】【" + beanName +"】");

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
