package com.gupaoedu.consumer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * second consumer config
 *
 * @author lp
 * @since 2021/2/4 22:27
 */
@Component
@PropertySource("classpath:gupaomq.properties")
@RabbitListener(queues = "${com.gupaoedu.secondqueue}", containerFactory = "rabbitListenerContainerFactory")
public class SecondConsumer {

    /**
     *  手动应答，直接用string接收
     * @author lp
     * @since 2021/2/5 10:01
     **/
    @RabbitHandler
    public void process(String msgContent, Channel channel, Message message) throws IOException {
        System.out.println("Second Queue received msg : " + msgContent);

        //手动应答
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
