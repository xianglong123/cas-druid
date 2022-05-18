package com.cas.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.cas.listener.DeleteCacheConsumer;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/18 9:46 上午
 * @desc
 */
@RequestMapping("/aop")
@RestController
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class AopController {

    @RequestMapping("/test")
    public String test() {
        System.out.println("aaa");
        BigDataController controller = SpringUtil.getBean(BigDataController.class);
        String update = controller.update(1);
        return "ok";
    }

}
