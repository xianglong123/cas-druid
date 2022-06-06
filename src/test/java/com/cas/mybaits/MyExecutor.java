package com.cas.mybaits;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/26 6:37 下午
 * @desc
 */
public class MyExecutor implements Executor {

    private final Executor delegate;

    private Executor wrapper;

    private static Map<String, List<Object>> cacheList = new HashMap<>();

    public MyExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public int update(MappedStatement ms, Object parameter) throws SQLException {
        return 0;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        if (cacheList.isEmpty()) {
            List<E> query = delegate.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            cacheList.put(ms.getId(), (List<Object>) query);
            return query;
        } else if (cacheList.containsKey(ms.getId())) {
            System.out.println("自定义一级缓存被使用");
            return (List<E>) cacheList.get(ms.getId());
        }
        return null;
    }

    @Override
    public <E> List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler) throws SQLException {
        return null;
    }

    @Override
    public <E> Cursor<E> queryCursor(MappedStatement ms, Object parameter, RowBounds rowBounds) throws SQLException {
        return null;
    }

    @Override
    public List<BatchResult> flushStatements() throws SQLException {
        return null;
    }

    @Override
    public void commit(boolean required) throws SQLException {

    }

    @Override
    public void rollback(boolean required) throws SQLException {

    }

    @Override
    public Transaction getTransaction() {
        return delegate.getTransaction();
    }


    @Override
    public void close(boolean forceRollback) {

    }

    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public CacheKey createCacheKey(MappedStatement ms, Object parameterObject, RowBounds rowBounds, BoundSql boundSql) {
        return delegate.createCacheKey(ms, parameterObject, rowBounds, boundSql);
    }

    @Override
    public boolean isCached(MappedStatement ms, CacheKey key) {
        return delegate.isCached(ms, key);
    }

    @Override
    public void deferLoad(MappedStatement ms, MetaObject resultObject, String property, CacheKey key, Class<?> targetType) {
        delegate.deferLoad(ms, resultObject, property, key, targetType);
    }

    @Override
    public void clearLocalCache() {
        delegate.clearLocalCache();
    }

    @Override
    public void setExecutorWrapper(Executor wrapper) {
        this.wrapper = wrapper;
    }
}
