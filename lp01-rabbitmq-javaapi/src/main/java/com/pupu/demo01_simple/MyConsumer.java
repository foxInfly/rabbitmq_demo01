package com.pupu.demo01_simple;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

/**
 * 消费者
 *
 * @author lp
 * @since 2021/2/1 11:31
 **/
public class MyConsumer {
    private final static String EXCHANGE_NAME = "SIMPLE_EXCHANGE";
    private final static String QUEUE_NAME = "SIMPLE_QUEUE";

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

        // 4. 声明一个direct交换机
        // String exchange, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", false, false, null);

        // 5. 声明队列
        // String queue,
        // boolean durable，没有持久化的队列，保存在内存中，服务重启后队列和消息都会消失（钱和人一起没了)),
        // boolean exclusive,exclusive:  排他性队列的特点是:
        //                                           1) 只对首次声明它的连接(Connection)可见
        //                                           2）会在其连接断开的时候自动删除。
        // boolean autoDelete,（autoDelete:没有消费者连接的时候，自动删除。）
        // Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 6. 将队列和交换机绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "gupao.best");

        System.out.println(" Waiting for message....");

        // 7. 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
                String msg = new String(body, StandardCharsets.UTF_8);
                System.out.println("Received message : '" + msg + "'");
                System.out.println("consumerTag : " + consumerTag);
                System.out.println("deliveryTag : " + envelope.getDeliveryTag()+"\n");
            }
        };

        // 8. 开始从queue获取消息
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

