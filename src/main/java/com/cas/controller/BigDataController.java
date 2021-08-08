package com.cas.controller;

import com.cas.service.BigDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/1 1:42 上午
 * @desc
 */
@RestController
public class BigDataController {

    @Resource
    private BigDataService bigDataService;

    @GetMapping("/add")
    public String add() {
        bigDataService.add();
        return "已完成";
    }

    @GetMapping("/queryById")
    public String queryById(String id) {
        bigDataService.queryById(id);
        return "已完成";
    }

}
