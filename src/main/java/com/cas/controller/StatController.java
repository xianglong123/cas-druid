package com.cas.controller;

import com.alibaba.druid.util.StringUtils;
import com.cas.bean.User;
import com.cas.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    @RequestMapping("/queryByName")
    @ResponseBody
    public String queryByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "name 不能为空";
        }
        User user = userMapper.queryByName(name);
        return user.getName();
    }


}
