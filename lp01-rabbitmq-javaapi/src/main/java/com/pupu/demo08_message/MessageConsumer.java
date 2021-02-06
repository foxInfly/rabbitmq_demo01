package com.pupu.demo08_message;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/**
 * 消息消费者
 * @author lp
 * @since  2021/2/6 23:00
 */
public class MessageConsumer {
    private final static String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        // 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" Waiting for demo08_message....");

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,byte[] body){
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received demo08_message : '" + msg + "' " );
                System.out.println("messageId : " + properties.getMessageId() );
                System.out.println(properties.getHeaders().get("name") + " -- " + properties.getHeaders().get("level"));
            }
        };

        // 开始获取消息
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
