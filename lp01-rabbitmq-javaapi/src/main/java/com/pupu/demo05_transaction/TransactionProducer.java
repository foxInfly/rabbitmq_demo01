package com.pupu.demo05_transaction;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者，测试事务模式。发送消息的效率比较低，建议使用Confirm模式
 * 参考文章：https://www.cnblogs.com/vipstone/p/9350075.html
 * 因事务模式指令比较多，消耗的性能也多
 *
 * @author lp
 * @since 2021/2/6 10:38
 **/
public class TransactionProducer {
    private final static String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception {
        //1. 根据uri创建连接工厂,连接，获取通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        //2. 声明消息内容
        String msg = "Hello world, Rabbit MQ";

        //3. 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);


        try {
            //4. 开启事务
            channel.txSelect();
            // 发送消息，发布了4条，但只确认了3条
            // String exchange, String routingKey, BasicProperties props, byte[] body
            channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
            channel.txCommit();
            channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
            channel.txCommit();
            channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
            channel.txCommit();
            channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
            int i = 1 / 0;
            channel.txCommit();
            channel.basicPublish("", QUEUE_NAME, null, (msg).getBytes());
            channel.txCommit();
            System.out.println("消息发送成功");
        } catch (Exception e) {
            channel.txRollback();
            System.out.println("消息已经回滚");
        }

        channel.close();
        conn.close();
    }
}

