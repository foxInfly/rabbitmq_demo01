package com.gupaoedu.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gupaoedu.entity.Merchant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbit sender
 * @author lp
 * @since  2021/2/4 22:31
 */
@Component
@PropertySource("classpath:gupaomq.properties")
public class RabbitSender {

    //1. exchange
    @Value("${com.gupaoedu.directexchange}")
    private String directExchange;
    @Value("${com.gupaoedu.topicexchange}")
    private String topicExchange;
    @Value("${com.gupaoedu.fanoutexchange}")
    private String fanoutExchange;

    //2. routing key
    @Value("${com.gupaoedu.directroutingkey}")
    private String directRoutingKey;
    @Value("${com.gupaoedu.topicroutingkey1}")
    private String topicRoutingKey1;
    @Value("${com.gupaoedu.topicroutingkey2}")
    private String topicRoutingKey2;


    //3. 自定义的模板，所有的消息都会转换成JSON发送
    @Resource
    AmqpTemplate amqpTemplate;

    public void send() throws JsonProcessingException {
        Merchant merchant =  new Merchant(1001,"a direct msg : 中原镖局","汉中省解放路266号");
        amqpTemplate.convertAndSend(directExchange,directRoutingKey, merchant);

        amqpTemplate.convertAndSend(topicExchange,topicRoutingKey1, "a topic msg : shanghai.gupao.teacher");
        amqpTemplate.convertAndSend(topicExchange,topicRoutingKey2, "a topic msg : changsha.gupao.student");

        // 发送JSON字符串
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(merchant);
        System.out.println(json);
        amqpTemplate.convertAndSend(fanoutExchange,"", json);
    }


}
