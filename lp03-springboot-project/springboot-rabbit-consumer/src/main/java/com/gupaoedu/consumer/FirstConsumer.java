package com.gupaoedu.consumer;

import com.gupaoedu.entity.Merchant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * first consumer config
 *
 * @author lp
 * @since 2021/2/4 22:26
 */
@Component
@PropertySource("classpath:gupaomq.properties") //加载指定配置文件
@RabbitListener(queues = "${com.gupaoedu.firstqueue}", containerFactory = "rabbitListenerContainerFactory")//监听队列及配置
public class FirstConsumer {

    /**
     * 处理消息
     * '@Payload：将接收到的消息转换成一个mechant对象'MessageConverter已经定义过了
     *
     * @author lp
     * @since 2021/2/5 9:40
     **/
    @RabbitHandler
    public void process(@Payload Merchant merchant) {
        System.out.println("First Queue received msg : " + merchant.toString());
    }

}
