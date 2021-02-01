package com.pupu.demo01_simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 *
 * @author lp
 * @since 2021/2/1 11:43
 **/
public class MyProducer {
    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";

    public static void main(String[] args) throws Exception {

        // 1. 通过ip、port、vhost、username、password构建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("114.55.95.30");
        factory.setPort(5673);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        // 2. 建立连接
        Connection conn = factory.newConnection();
        // 3. 创建消息通道
        Channel channel = conn.createChannel();

        // 4. 发送消息
        String msg = "Hello world, I'm Rabbit MQ";

        // 5. begin publis message
        // String exchange, String routingKey, BasicProperties props, byte[] body
        channel.basicPublish(EXCHANGE_NAME, "gupao.best", null, msg.getBytes());

        channel.close();
        conn.close();
    }
}

