package com.cas.config.dynamic;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: springboot
 * @description: 动态数据源配置
 * @author: gourd
 * @date: 2018-11-22 14:01
 * @since: 1.0
 **/
@Configuration
public class DynamicDataSourceConfig {
    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceConfig.class);
    /**
     * 创建 TargetDataSource Bean
     */
    @Bean
    @ConfigurationProperties("spring.datasource.master")
    public DataSource masterDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.slave")
    public DataSource slaveDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 如果还有数据源,在这继续添加 TargetDataSource Bean
     */
    @Bean
    @Primary
    public DynamicDataSource myDataSource(@Qualifier("masterDataSource")DataSource masterDataSource, @Qualifier("slaveDataSource")DataSource slaveDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(CommonConstant.MASTER_DATASOURCE, masterDataSource);
        targetDataSources.put(CommonConstant.SLAVE_DATASOURCE, slaveDataSource);
        // 还有数据源,在targetDataSources中继续添加
        log.info("^o^= DataSources:" + targetDataSources);
        //默认的数据源是oneDataSource
        return new DynamicDataSource(masterDataSource, targetDataSources);
    }

}
