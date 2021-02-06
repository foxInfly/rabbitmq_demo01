package com.pupu.demo06_confirm;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 普通确认模式
 *
 * @author lp
 * @since 2021/2/1 17:47
 **/
public class Demo1_NormalConfirmProducer {

    private final static String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception {

        //1. 根据uri创建连接工厂,连接，获取通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        //2. 声明消息内容
        String msg = "Hello world, Rabbit MQ ,Normal Confirm";

        //3. 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //4. 开启发送方确认模式
        channel.confirmSelect();

        //5. 发布消息
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        // 普通Confirm，发送一条，确认一条
        if (channel.waitForConfirms()) {
            System.out.println("消息发送成功");
        } else {
            System.out.println("消息发送失败");
        }

        channel.close();
        conn.close();
    }
}
