package com.cas.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 16:43 2020-05-11
 * @version: V1.0
 * @review: 线程池复用
 */
@Component
public class ThreadPoolUtil {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtil.class);

    private static  ThreadPoolExecutor poolExecutor = null;

    {
        // 核心线程池5，最长闲置时间30秒，拒绝策略：忽略
        poolExecutor = new ThreadPoolExecutor(5, 5, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(12), new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * 调用线程池的方法添加任务
     * @param commond
     */
    public void execute(Runnable commond) {
        if (poolExecutor == null) {
            poolExecutor = new ThreadPoolExecutor(5, 5, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(12), new ThreadPoolExecutor.DiscardPolicy());
        }
        poolExecutor.execute(commond);
    }

    public Future<?> submit(Runnable commond) {
        if (poolExecutor == null) {
            poolExecutor = new ThreadPoolExecutor(5, 5, 30L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(12), new ThreadPoolExecutor.DiscardPolicy());
        }
        return poolExecutor.submit(commond);
    }

    /**
     * 释放线程池资源
     */
    public void shutdown() {
        if(poolExecutor != null) {
            log.info("正在执行任务总量-[{}]", poolExecutor.getActiveCount());
            poolExecutor.shutdown();
        }
    }

    public boolean isShutdown() {
        if(poolExecutor == null) {
            return true;
        }
        return poolExecutor.isShutdown();
    }

}
