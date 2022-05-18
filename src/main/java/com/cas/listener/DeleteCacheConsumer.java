package com.cas.listener;

import com.cas.util.RedisUtil;
import com.rabbitmq.client.Channel;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.cas.config.mq.RabbitConfig.DICP_COMMAND_DELETE_CACHE;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/4/12 10:36 上午
 * @desc 延迟双删消费者
 */
@Aspect
@Component
public class DeleteCacheConsumer {
    private static final Logger log = LoggerFactory.getLogger(DeleteCacheConsumer.class);

    @Resource
    private RedisUtil redisUtil;

    @RabbitListener(queues = DICP_COMMAND_DELETE_CACHE)
    public void process(Message message, Channel channel) throws IOException {
        String key = new String(message.getBody());
        try {
            log.info("当前时间： [{}]", LocalDateTime.now());
            // 1、删除key
            redisUtil.del(key);
            System.out.println("aaa  :  " + key);
        } catch (Exception e) {
            // 出问题，重试队列继续放
            log.error("redis缓存删除失败，key=[{}]", key);
            e.printStackTrace();
        } finally {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

}
