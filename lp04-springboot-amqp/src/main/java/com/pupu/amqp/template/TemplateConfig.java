package com.pupu.amqp.template;

import com.pupu.util.ResourceUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Template Config
 *
 * @author lp
 * @since 2021/2/6 14:31
 **/
@Configuration//用于定义配置类，可替换xml配置文件
public class TemplateConfig {

    /**
     * 创建连接工厂
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        return cachingConnectionFactory;
    }

    /**
     * RabbitAdmin是AmqpAdmin的实现，封装了对RabbitMQ的基础管理操作，比如对交换机、队列、绑定的声明和删除等。代替Channel
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * 是 AmqpTemplate的一个实现（目前为止也是唯一的实现），用来简化消息的收发，支持消息的确认(Confirm）与返回（Return）。
     * 它封装了创建连接、创建消息信道、收发消息、消息格式转换（ConvertAndSend→Message） 、关闭信道、关闭连接等等操作
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);// 消息是否必须路由到一个队列中，配合Return使用

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("回发的消息：");
            System.out.println("replyCode: " + replyCode);
            System.out.println("replyText: " + replyText);
            System.out.println("exchange: " + exchange);
            System.out.println("routingKey: " + routingKey);
        });

        rabbitTemplate.setChannelTransacted(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("发送消息失败：" + cause);
                throw new RuntimeException("发送异常：" + cause);
            }
        });


        return rabbitTemplate;
    }
}