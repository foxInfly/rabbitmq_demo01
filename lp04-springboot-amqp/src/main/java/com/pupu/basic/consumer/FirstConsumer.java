package com.pupu.basic.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: qingshan
 * @Description: 咕泡学院，只为更好的你
 */
@Component
@RabbitListener(queues = "GP_BASIC_FIRST_QUEUE")
public class FirstConsumer {

    @RabbitHandler
    public void process(String msg, Channel channel,long deliveryTag) throws IOException {
        channel.basicAck(deliveryTag, true);
        System.out.println(" first queue received msg : " + msg);
    }
}
