package com.cas;

import com.cas.config.dynamic.DynamicDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:34 2021/3/14
 * @version: V1.0
 * @review:
 */
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = "com.cas")
@MapperScan(basePackages = "com.cas.dao")
public class DruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class, args);
    }

}
