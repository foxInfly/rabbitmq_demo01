package com.gupaoedu.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rabbit config
 *
 * @author lp
 * @since 2021/2/4 22:33
 */
@Configuration
public class RabbitConfig {
    /**
     * 所有的消息发送都会转换成JSON格式发到交换机
     *
     * @param connectionFactory connectionFactory
     */
    @Bean
    public RabbitTemplate gupaoTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
//        rabbitTemplate.setChannelTransacted(true);//开启事务
        //开启异步确认模式
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.out.println("发送消息失败: " + cause);
                throw new RuntimeException("发送异常:" + cause);

            }
        });
        //开启回发给生产者
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("回发的消息:");
                System.out.println("replyCode: "+replyCode);
                System.out.println("replyText: "+replyText);
                System.out.println("exchange: " +exchange);
                System.out.println("routingKey: "+routingKey );

            }
        });
        return rabbitTemplate;
    }
}
