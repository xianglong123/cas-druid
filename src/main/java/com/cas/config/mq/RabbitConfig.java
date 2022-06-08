package com.cas.config.mq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/12/13 9:43 上午
 * @desc 涉及队列
 */
//@Configuration
public class RabbitConfig {




    // -------------------------------- 延迟双删队列 ---------------------------------------------

    /**
     * 结果查询延时队列【3s】
     */
    public static final String MIRROR_DICP_COMMAND_QUERY_DELAY_3_S = "mirror.dicp-command.query-delay-3s";
    public static final String MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_EXCHANGE = "mirror.dicp-command.query-delay-3s-exchange";
    public static final String MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_ROUTING_KEY = "mirror.dicp-command.query-delay-3s-routingKey";

    /**
     * 删除缓存队列
     */
    public static final String DICP_COMMAND_DELETE_CACHE = "mirror.dicp-command.delete-cache";
    public static final String DICP_COMMAND_DELETE_CACHE_EXCHANGE = "mirror.dicp-command.delete-cache-exchange";
    public static final String DICP_COMMAND_DELETE_CACHE_ROUTING_KEY = "mirror.dicp-command.delete-cache-routingKey";

    @Bean("queryDelay3sQueue")
    public Queue queryDelay3sQueue(){
        Map<String, Object> args = new HashMap<>(3);
        // x-dead-letter-exchange    这里声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", DICP_COMMAND_DELETE_CACHE_EXCHANGE);
        // x-dead-letter-routing-key  这里声明当前队列的死信路由key
        args.put("x-dead-letter-routing-key", DICP_COMMAND_DELETE_CACHE_ROUTING_KEY);
        // x-message-ttl  声明队列的TTL
        args.put("x-message-ttl", 3000);
        return QueueBuilder.durable(MIRROR_DICP_COMMAND_QUERY_DELAY_3_S).withArguments(args).build();
    }

    @Bean("deleteCacheQueue")
    public Queue deleteCacheQueue(){
        return new Queue(DICP_COMMAND_DELETE_CACHE);
    }

    @Bean("deleteCacheExchange")
    public DirectExchange deleteCacheExchange(){
        return new DirectExchange(DICP_COMMAND_DELETE_CACHE_EXCHANGE);
    }

    @Bean("queryDelay3sExchange")
    public DirectExchange queryDelay3sExchange(){
        return new DirectExchange(MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_EXCHANGE);
    }

    @Bean
    public Binding deleteCacheBinding(@Qualifier("deleteCacheQueue") Queue queue,
                                @Qualifier("deleteCacheExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(DICP_COMMAND_DELETE_CACHE_ROUTING_KEY);
    }

    @Bean
    public Binding queryDelay3sBinding(@Qualifier("queryDelay3sQueue") Queue queue,
                                      @Qualifier("queryDelay3sExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MIRROR_DICP_COMMAND_QUERY_DELAY_3_S_ROUTING_KEY);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 手动确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}
