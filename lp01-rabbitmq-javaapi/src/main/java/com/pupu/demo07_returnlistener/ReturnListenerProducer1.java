package com.pupu.demo07_returnlistener;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.*;

//import java.util.HashMap;
//import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 当消息无法匹配到队列时，会发回给生产者
 *
 * @author lp
 * @since 2021/2/6 11:21
 **/
public class ReturnListenerProducer1 {
    public static void main(String[] args) throws Exception {
        //1. 根据uri创建连接工厂,连接，获取通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //2. 设置回发监听
        channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
            System.out.println("=========监听器收到了无法路由，被返回的消息============");
            System.out.println("replyText:" + replyText);
            System.out.println("exchange:" + exchange);
            System.out.println("routingKey:" + routingKey);
            System.out.println("message:" + new String(body));
        });

        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder().deliveryMode(2).contentEncoding("UTF-8").build();

        // 备份交换机
        channel.exchangeDeclare("ALTERNATE_EXCHANGE", "topic", false, false, false, null);
        channel.queueDeclare("ALTERNATE_QUEUE", false, false, false, null);
        channel.queueBind("ALTERNATE_QUEUE", "ALTERNATE_EXCHANGE", "#");

        // 在声明交换机的时候指定备份交换机
//        Map<String, Object> arguments = new HashMap<>();
//        arguments.put("alternate-exchange", "ALTERNATE_EXCHANGE");
        channel.exchangeDeclare("TEST_EXCHANGE", "topic", false, false, false, null);


        // 发送到了默认的交换机上，由于没有任何队列使用这个关键字跟交换机绑定，所以会被退回
        // 第三个参数是设置的mandatory，如果mandatory是false，消息也会被直接丢弃
        channel.basicPublish("TEST_EXCHANGE", "qingshan2673", true, properties, "只为更好的你".getBytes());

        TimeUnit.SECONDS.sleep(10);

        channel.close();
        connection.close();
    }
}
