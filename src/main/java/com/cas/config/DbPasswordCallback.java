package com.cas.config;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.alibaba.druid.util.StringUtils;
import com.cas.util.AesHopeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午10:50 2021/3/15
 * @version: V1.0
 * @review: 环境：application-dev.yaml
 */
@Slf4j
@Component
public class DbPasswordCallback extends DruidPasswordCallback {

    /**
     * 需要配合配置里面的password-callback-class-name: com.cas.config.DbPasswordCallback 一起使用
     *【多数据源暂时没有实现密码加密】
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String password = properties.getProperty("password");
        String publicKey = properties.getProperty("publicKey");
        if (!StringUtils.isEmpty(password)) {
            try {
                // 所以这里的代码是将密码进行解密
                String decryt = AesHopeUtil.decryt(publicKey, password);
                assert decryt != null;
                setPassword(decryt.toCharArray());
            } catch (Exception e) {
                setPassword(password.toCharArray());
            }
        }
    }

    /**
     * 生成加密后的密码，放到yml中
     * @param args
     */
    public static void main(String[] args) {
        // 生成加密后的密码，放到yml中
        String password = "123456";
        String pwd = AesHopeUtil.encrypt("GOURD-HXNLYW-201314", password);
        System.out.println(pwd);

        String source = AesHopeUtil.decryt("GOURD-HXNLYW-201314", pwd);
        System.out.println(source);
    }
}
