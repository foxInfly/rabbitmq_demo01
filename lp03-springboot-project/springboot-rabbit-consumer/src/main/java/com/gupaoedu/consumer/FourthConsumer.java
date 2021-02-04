package com.gupaoedu.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author lp
 * @since  2021/2/4 22:26
 */
@Component
@PropertySource("classpath:gupaomq.properties")
@RabbitListener(queues = "${com.gupaoedu.fourthqueue}", containerFactory="rabbitListenerContainerFactory")
public class FourthConsumer {
    @RabbitHandler
    public void process(String message) {

        System.out.println("Fourth Queue received msg : " + message);
    }
}
