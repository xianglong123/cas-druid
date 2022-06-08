package com.cas.config.interceptor;

import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cas.bean.BigData;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
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
//                @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
                @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
        })
public class InfoMybatisInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(InfoMybatisInterceptor.class);
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("mybatis拦截器触发");
        Object proceed = invocation.proceed();
        if (proceed instanceof Collection) {
            List<Object> resList = new ArrayList<>();
            DefaultResultSetHandler defaultResultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
            MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(defaultResultSetHandler, "mappedStatement");
            List<ResultMap> resultMaps = mappedStatement.getResultMaps();
            int resultMapCount = resultMaps.size();
            // 获取当前resultType的类型
            Class<?> resultType = resultMaps.get(0).getType();
            if (true) {
                Object[] obj = invocation.getArgs();
                Statement statement = (Statement) obj[0];
                // 获取结果集
                ResultSet resultSet = statement.getResultSet();

                if (resultSet != null) {
                    // 获取队名列名
                    ResultSetMetaData rsmd = resultSet.getMetaData();
                    List<String> columnList = new ArrayList<>();

                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        columnList.add(rsmd.getColumnName(i));
                    }

                    while (resultSet.next()) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        for (String colName: columnList) {
                            map.put(colName, resultSet.getObject(colName));
                        }
                        JSONObject.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
                        log.info("result=[{}]", JSON.toJSONString(map, SerializerFeature.WriteMapNullValue,
                                SerializerFeature.DisableCircularReferenceDetect,
                                SerializerFeature.WriteDateUseDateFormat));
                        resList.add(proceed);
                    }

                }
            }
            return resList;
        } else {
            return proceed;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
