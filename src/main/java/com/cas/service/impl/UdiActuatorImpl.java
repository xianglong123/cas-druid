package com.cas.service.impl;

import com.cas.bean.User;
import com.cas.dao.UserMapper;
import com.cas.service.UdiActuator;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午9:52 2021/3/23
 * @version: V1.0
 * @review: 这里测试sqlSession执行sql的两种方式
 * 第一种更加灵活
 * 第二种更加符合对象的思想
 */
@Slf4j
@Service
public class UdiActuatorImpl implements UdiActuator {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 第一种执行sql的方式
     * 通过sqlSession执行sql
     */
    @Override
    public void query() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Map<String, String> map = new HashMap<>();
        map.put("name", "Sandy");
        User obj = sqlSession.selectOne("queryByName", map);
        log.info("输出结果【{}】", obj.toString());
    }

    /**
     *
     * 第二种执行sql的方式【推荐】
     * 通过sqlSession执行sql
     *  顺带测试一级缓存:
     *  sqlSession.clearCache();清空缓存
     */
    @Override
    public void query2() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User sandy = mapper.queryByName("Sandy");
        User sandy2 = mapper.queryByName("Sandy");
        User sandy3 = mapper.queryByName("Sandy");
        log.info("输出结果【{}】", sandy);
        log.info("输出结果【{}】", sandy2);
        log.info("输出结果【{}】", sandy3);
    }


}
