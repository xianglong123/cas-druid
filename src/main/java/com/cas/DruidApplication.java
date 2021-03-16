package com.cas;

import com.cas.config.DynamicDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:34 2021/3/14
 * @version: V1.0
 * @review:
 */
@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@MapperScan(basePackages = "com.cas.dao")
public class DruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class, args);
    }

}
