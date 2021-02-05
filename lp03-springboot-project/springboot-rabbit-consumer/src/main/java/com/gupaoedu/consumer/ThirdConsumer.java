package com.gupaoedu.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * third consumer config
 *
 * @author lp
 * @since 2021/2/4 22:27
 */
@Component
@PropertySource("classpath:gupaomq.properties")
@RabbitListener(queues = "${com.gupaoedu.thirdqueue}", containerFactory = "rabbitListenerContainerFactory")
public class ThirdConsumer {

    /**直接接收，未应答*/
    @RabbitHandler
    public void process(String msg) {
        System.out.println("Third Queue received msg : " + msg);
    }
}
