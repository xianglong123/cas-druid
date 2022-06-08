package com.cas.config.mybatis;

import com.cas.service.Desensitize;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/6/8 11:19 上午
 * @desc
 */
@Configuration
public class DesensiteConfig implements Desensitize {

    @Override
    public String encryptData(String s) {
        return s + "123";
    }

    @Override
    public String decryptData(String s) {
        return s + "456";
    }
}
