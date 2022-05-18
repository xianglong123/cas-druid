package com.cas.controller;

import com.alibaba.fastjson.JSONObject;
import com.cas.aspect.QueryCache;
import com.cas.bean.BigData;
import com.cas.enums.CacheEnum;
import com.cas.service.BigDataService;
import com.cas.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @Resource
    private RedisUtil redisUtil;


    @GetMapping("/add")
    public String add() {
        bigDataService.add();
        return "已完成";
    }

    @PostMapping("/queryById")
    @QueryCache(CacheEnum.BIG_DATA)
    public BigData queryById(requestDTO requestDTO) {
        return bigDataService.queryById(requestDTO.getId());
    }

    @GetMapping("/update")
    public String update(int id) {
        bigDataService.update(id);
        return "update已完成";
    }


    public class requestDTO {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
