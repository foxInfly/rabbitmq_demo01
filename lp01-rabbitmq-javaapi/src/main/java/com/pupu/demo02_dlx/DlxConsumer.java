package com.pupu.demo02_dlx;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 消费者，由于消费的代码被注释掉了，
 * 10秒钟后，消息会从正常队列 GP_ORI_USE_QUEUE 到达死信交换机 GP_DEAD_LETTER_EXCHANGE ，然后路由到死信队列 GP_DEAD_LETTER_QUEUE
 *
 * @author lp
 * @since 2021/2/1 23:01
 */
public class DlxConsumer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        //1. 指定队列的死信交换机
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "GP_DEAD_LETTER_EXCHANGE");
        // arguments.put("x-expires",9000L); // 设置队列的TTL
        // arguments.put("x-max-length", 4); // 如果设置了队列的最大长度，超过长度时，先入队的消息会被发送到DLX

        //2. 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare("GP_ORI_USE_QUEUE", false, false, false, arguments);

        //3. 声明死信交换机
        channel.exchangeDeclare("GP_DEAD_LETTER_EXCHANGE", "topic", false, false, false, null);
        //4. 声明死信队列
        channel.queueDeclare("GP_DEAD_LETTER_QUEUE", false, false, false, null);
        //5. 绑定，此处 Dead letter routing key 设置为 #
        channel.queueBind("GP_DEAD_LETTER_QUEUE", "GP_DEAD_LETTER_EXCHANGE", "#");
        System.out.println(" Waiting for message....");

        // 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message : '" + msg + "'");
            }
        };

        // 开始获取消息
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume("GP_DEAD_LETTER_QUEUE", true, consumer);
    }
}