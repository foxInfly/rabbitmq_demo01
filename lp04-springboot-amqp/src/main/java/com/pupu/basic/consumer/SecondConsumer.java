package com.pupu.basic.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: qingshan
 * @Description: 咕泡学院，只为更好的你
 */
@Component
@RabbitListener(queues = "GP_BASIC_SECOND_QUEUE")
public class SecondConsumer {

    @RabbitHandler
    public void process(String msg){
        System.out.println(" second queue received msg : " + msg);
    }
}
