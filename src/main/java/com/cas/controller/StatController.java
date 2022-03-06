package com.cas.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.druid.util.StringUtils;
import com.cas.bean.User;
import com.cas.config.aop.LogHistory;
import com.cas.config.dynamic.TargetDataSource;
import com.cas.dao.AccountMapper;
import com.cas.dao.UserMapper;
import com.cas.service.UdiActuator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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
    private UdiActuator udiActuator;

    @RequestMapping("/queryByName")
    @ResponseBody
    @LogHistory
    @TargetDataSource
    public String queryByName(String name) {
        if (StringUtils.isEmpty(name)) {
            return "name 不能为空";
        }
        testSqlSession();
        return "ok";
    }

    /**
     * 测试一级缓存和sqlSession执行方式
     */
    private void testSqlSession() {
        udiActuator.query2();
    }

    /**
     * 测试二级缓存
     *
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

    @GetMapping("export")
    public void export(HttpServletResponse response) throws IOException {
        String url = "http://localhost:8088/export";
        LocalDate today = LocalDate.now();
        String name = today.minus(1, ChronoUnit.WEEKS).format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" +today.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-Dial_Test";
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + name + ".xlsx");
        HttpResponse execute = HttpRequest.get(url).execute();
        ServletOutputStream out = response.getOutputStream();
        out.write(execute.bodyBytes());
    }


}
