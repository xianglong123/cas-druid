package com.cas;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 14:29 2020-01-20
 * @version: V1.0
 * @review:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationTest.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Test
    public void tes(){
        threadLocal.set("name");
        System.out.println(threadLocal.get());
        threadLocal.remove();
    }
}
