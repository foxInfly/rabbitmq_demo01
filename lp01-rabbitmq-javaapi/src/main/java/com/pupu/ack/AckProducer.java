package com.pupu.ack;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者，用于测试消费者手工应答和重回队列
 *
 * @author lp
 * @since 2021/2/1 14:18
 **/
public class AckProducer {
    private final static String QUEUE_NAME = "TEST_ACK_QUEUE";

    public static void main(String[] args) throws Exception {
        // 1. 根据uri创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));

        // 2. 建立连接
        Connection conn = factory.newConnection();
        // 3. 创建消息通道
        Channel channel = conn.createChannel();

        String msg = "test ack message ";
        // 4. 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发送消息
        // String exchange, String routingKey, BasicProperties props, byte[] body
        for (int i = 0; i < 5; i++) {
            channel.basicPublish("", QUEUE_NAME, null, (msg + i).getBytes());
        }

        channel.close();
        conn.close();
    }
}

