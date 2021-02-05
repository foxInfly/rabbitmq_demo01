package com.pupu.demo01_simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 简单收发消息测试
 *
 * @author lp
 * @since 2021/2/1 11:24
 **/
public class DeleteTestAllData {

    public static void main(String[] args) throws IOException, TimeoutException {
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

        String[] queueNames = {"ORIGIN_QUEUE", "GP_FIRST_QUEUE", "GP_FOURTH_QUEUE", "GP_SECOND_QUEUE", "GP_THIRD_QUEUE",
                "MY_FIRST_QUEUE", "MY_FOURTH_QUEUE", "MY_SECOND_QUEUE", "MY_THIRD_QUEUE",
                "SIMPLE_QUEUE", "TEST_TTL_QUEUE", "TEST_DLX_QUEUE", "DLX_QUEUE",
                "GP_ORI_USE_QUEUE", "GP_DEAD_LETTER_QUEUE", "DELAY_QUEUE", "TEST_LIMIT_QUEUE", "ALTERNATE_QUEUE"};

        String[] exchangeNames = {"GP_DIRECT_EXCHANGE", "GP_FANOUT_EXCHANGE", "GP_TOPIC_EXCHANGE",
                "MY_DIRECT_EXCHANGE", "MY_FANOUT_EXCHANGE", "MY_TOPIC_EXCHANGE",
                "SIMPLE_EXCHANGE", "DLX_EXCHANGE", "GP_ORI_USE_EXCHANGE", "GP_DEAD_LETTER_EXCHANGE",
                "DELAY_EXCHANGE", "SIMPLE_EXCHANGE", "ALTERNATE_EXCHANGE", "TEST_EXCHANGE"};

        for (String queueName : queueNames) {
            channel.queueDelete(queueName);
        }

        for (String exchangeName : exchangeNames) {
            channel.exchangeDelete(exchangeName);
        }

        channel.close();
        conn.close();

    }
}
