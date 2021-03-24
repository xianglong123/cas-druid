package com.cas.config.dynamic;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @program: springboot
 * @description: 创建动态数据源
 * 实现数据源切换的功能就是自定义一个类扩展AbstractRoutingDataSource抽象类，
 * 其实该相当于数据源DataSourcer的路由中介,
 * 可以实现在项目运行时根据相应key值切换到对应的数据源DataSource上。
 * @author: gourd
 * @date: 2018-11-22 13:59
 * @since: 1.0
 **/
public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * 配置DataSource, defaultTargetDataSource为主数据库
     */
    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        //设置默认数据源
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        //设置数据源列表
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    /**
     * 是实现数据源切换要扩展的方法，
     * 该方法的返回值就是项目中所要用的DataSource的key值，
     * 拿到该key后就可以在resolvedDataSource中取出对应的DataSource，
     * 如果key找不到对应的DataSource就使用默认的数据源。
     * */
    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    /**
     * 绑定当前线程数据源路由的key
     * 使用完成后必须调用removeRouteKey()方法删除
     */
    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    /**
     * 获取当前线程的数据源路由的key
     */
    public static String getDataSource() {
        return contextHolder.get();
    }

    /**
     * 删除与当前线程绑定的数据源路由的key
     */
    public static void clearDataSource() {
        contextHolder.remove();
    }

}
