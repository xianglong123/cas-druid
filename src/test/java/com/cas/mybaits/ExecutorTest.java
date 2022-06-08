package com.cas.mybaits;

import cn.hutool.extra.spring.SpringUtil;
import com.cas.BaseTest;
import com.cas.bean.Account;
import com.cas.bean.BigData;
import com.cas.bean.User;
import com.cas.dao.BigDataMapper;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.IfSqlNode;
import org.apache.ibatis.scripting.xmltags.StaticTextSqlNode;
import org.apache.ibatis.scripting.xmltags.WhereSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/26 3:45 下午
 * @desc
 */
public class ExecutorTest extends BaseTest {

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    private Configuration configuration;

    private Connection connection;

    @Before
    public void init() {
        configuration = sqlSessionFactory.getConfiguration();
        connection = sqlSessionFactory.openSession().getConnection();
    }

    @Test
    public void simpleTest() throws SQLException {
        SimpleExecutor executor = new SimpleExecutor(configuration, new JdbcTransaction(connection));
        MappedStatement ms = configuration.getMappedStatement("com.cas.dao.BigDataMapper.queryById");
        List<Object> list = executor.doQuery(ms, 1, RowBounds.DEFAULT,
                SimpleExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        executor.doQuery(ms, 1, RowBounds.DEFAULT,
                SimpleExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        System.out.println(list.get(0));
    }

    // 可重用执行器
    @Test
    public void ReuseTest() throws SQLException {
        ReuseExecutor executor = new ReuseExecutor(configuration, new JdbcTransaction(connection));
        MappedStatement ms = configuration.getMappedStatement("com.cas.dao.BigDataMapper.queryById");
        List<Object> list = executor.doQuery(ms, 1, RowBounds.DEFAULT,
                SimpleExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        executor.doQuery(ms, 1, RowBounds.DEFAULT,
                SimpleExecutor.NO_RESULT_HANDLER, ms.getBoundSql(1));
        System.out.println(list.get(0));
    }

    // 批处理执行器
    // 只针对更新操作
    @Test
    public void BatchTest() throws SQLException {
        BatchExecutor executor = new BatchExecutor(configuration, new JdbcTransaction(connection));
        MappedStatement ms = configuration.getMappedStatement("com.cas.dao.BigDataMapper.update");
        Map<String, Object> param = new HashMap<>();
        param.put("id", 1);
        param.put("detail", "batch");
        int i = executor.doUpdate(ms, param);
        int j = executor.doUpdate(ms, param);
        executor.doFlushStatements(false);
    }

    // 基础执行器
    @Test
    public void BaseExecutorTest() throws SQLException {
        Executor executor = new SimpleExecutor(configuration, new JdbcTransaction(connection));
        MappedStatement ms = configuration.getMappedStatement("com.cas.dao.BigDataMapper.queryById");
        List<Object> list = executor.query(ms, 1, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
        executor.query(ms, 1, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
        System.out.println(list.get(0));
    }

    /**
     * 二级缓存
     * 装饰器模式
     * @throws SQLException
     */
    @Test
    public void cacheExecutorTest() throws SQLException {
        Executor executor = new SimpleExecutor(configuration, new JdbcTransaction(connection));
        // 装饰器模式
        CachingExecutor cachingExecutor = new CachingExecutor(executor);
        MappedStatement ms = configuration.getMappedStatement("com.cas.dao.BigDataMapper.queryById");
        cachingExecutor.query(ms, 1, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
        cachingExecutor.commit(true);
        cachingExecutor.query(ms, 1, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
    }

    /**
     * 自己创造一级缓存
     * 装饰器模式
     * @throws SQLException
     */
    @Test
    public void cacheOneExecutorTest() throws SQLException {
        Executor executor = new SimpleExecutor(configuration, new JdbcTransaction(connection));
        // 装饰器模式
        MyExecutor myExecutor = new MyExecutor(executor);
        CachingExecutor cachingExecutor = new CachingExecutor(myExecutor);
        MappedStatement ms = configuration.getMappedStatement("com.cas.dao.BigDataMapper.queryById");
        cachingExecutor.query(ms, 1, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
        cachingExecutor.commit(true);
        cachingExecutor.query(ms, 1, RowBounds.DEFAULT, Executor.NO_RESULT_HANDLER);
    }

    @Test
    public void sessionTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        List<Object> list = sqlSession.selectList("com.cas.dao.BigDataMapper.queryById", 1);
        System.out.println(list.get(0));
    }

    @Test
    public void testBySpring() {
        BigDataMapper mapper = SpringUtil.getBean(BigDataMapper.class);
        // spring 不开启事务会导致一级缓存失效
        DataSourceTransactionManager txManager = SpringUtil.getBean(DataSourceTransactionManager.class);
        TransactionStatus transaction = txManager.getTransaction(new DefaultTransactionDefinition());
        BigData bigData = mapper.queryById(1);
        BigData bigData2 = mapper.queryById(1);
        System.out.println("=====" + (bigData == bigData2));
    }

    @Test
    public void test() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true );

        List<Object> list = new ArrayList<>();
        ResultHandler handler = resultContext -> {
            if (resultContext.getResultCount() > 1) {
                resultContext.stop();
            }
            list.add(resultContext.getResultObject());
        };
        sqlSession.select("com.cas.dao.BigDataMapper.queryById", 1,handler);
        System.out.println(list.size());
    }

    @Test
    public void ifTest() {
        Account account = new Account();
        account.setId("1");
        DynamicContext context = new DynamicContext(configuration, account);

        // 静态节点逻辑
        new StaticTextSqlNode("select * from account ").apply(context);

        // where
        IfSqlNode ifSqlNode = new IfSqlNode(new StaticTextSqlNode(" and id=#{id}"), "id!=null");

        WhereSqlNode where = new WhereSqlNode(configuration, ifSqlNode);
        // 添加where逻辑
        where.apply(context);

        System.out.println(context.getSql());
    }

    @Test
    public void foreachTest() {
        HashMap<Object, Object> parameter = new HashMap<>();
        parameter.put("list", Arrays.asList("1", "2"));
        sqlSessionFactory.openSession().selectList("findByIds", parameter);
    }


}
