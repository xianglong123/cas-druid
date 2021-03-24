package com.cas.controller;

import com.alibaba.druid.util.StringUtils;
import com.cas.bean.Account;
import com.cas.bean.User;
import com.cas.config.dynamic.CommonConstant;
import com.cas.config.dynamic.TargetDataSource;
import com.cas.config.aop.LogHistory;
import com.cas.dao.AccountMapper;
import com.cas.dao.UserMapper;
import com.cas.service.UdiActuator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:37 2021/3/14
 * @version: V1.0
 * @review:
 */
@Controller
public class StatController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UdiActuator udiActuator;

    @RequestMapping("/queryByName")
    @ResponseBody
    @LogHistory
    @TargetDataSource
    public String queryByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "name 不能为空";
        }
        User user = userMapper.queryByName(name);
        return user.getName();
    }

    /**
     * 测试一级缓存和sqlSession执行方式
     */
    private void testSqlSession() {
        udiActuator.query2();
    }

    /**
     * 测试二级缓存
     * @param name
     */
    private void testCache2(String name) {
        User user = userMapper.queryByName(name);
        User user2 = userMapper.queryByName(name);
        System.out.println("====" + user);
        System.out.println("====" + user2);
    }

    @RequestMapping("/updateAge")
    @ResponseBody
    @TargetDataSource
    public String updateAge() {
        return String.valueOf(userMapper.updateAge());
    }

    @RequestMapping("/queryByUserId")
    @ResponseBody
    @TargetDataSource(value = CommonConstant.SLAVE_DATASOURCE)
    public String queryById(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return "name 不能为空";
        }
        Account account = accountMapper.queryById(userId);
        return account.getId();
    }

}
