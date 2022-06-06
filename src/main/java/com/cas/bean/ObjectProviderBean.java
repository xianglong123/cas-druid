package com.cas.bean;

import com.cas.service.BigDataService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/31 10:32 下午
 * @desc 通过 ObjectProvider 获取接口实现的实例对象（spring容器里面的）
 */
@Component
public class ObjectProviderBean {
    private BigDataService[] bigDataServices;

    @Bean
    public User user(ObjectProvider<BigDataService[]> bigDataServices) {
        this.bigDataServices = bigDataServices.getIfAvailable();
        return new User();
    }

}
