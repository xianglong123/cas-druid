package com.cas.config.interceptor;

import com.cas.config.dynamic.CommonConstant;
import com.cas.config.dynamic.DataSourceAspect;
import com.cas.config.dynamic.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午10:39 2021/3/15
 * @version: V1.0
 * @review: mybatis拦截器
 */
//@Component
@Intercepts(
        {
                @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
                @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = StatementHandler.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
                @Signature(type = ParameterHandler.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        })
public class MybatisInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(MybatisInterceptor.class);
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.warn("bbbbbb");
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            // TODO: 自己的逻辑
        }

        Object[] objects = invocation.getArgs();
        MappedStatement ms = (MappedStatement)objects[0];

        String dynamicDataSourceGlobal = null;

        // 读方法
        if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
            dynamicDataSourceGlobal = CommonConstant.MASTER_DATASOURCE;
        } else {
            // 写数据源
            dynamicDataSourceGlobal = CommonConstant.SLAVE_DATASOURCE;
        }

        System.out.println("-------------------------------");
        System.out.println("方法[{" + ms.getId() + "}] 使用了[{" + dynamicDataSourceGlobal + "}] 数据源，类型[{" + ms.getSqlCommandType() + "}]");

        // 设置数据源
        DynamicDataSource.setDataSource(dynamicDataSourceGlobal);

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身，减少目标被代理的次数。
//        return (target instanceof RoutingStatementHandler) ? Plugin.wrap(target, this) : target;
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
