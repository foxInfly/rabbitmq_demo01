package com.pupu.ttl;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *  生产者，通过TTL测试死信队列
 *
 * @author lp
 * @since 2021/2/2 10:10
 **/
public class TTLProducer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        String msg = "Hello world, Rabbit MQ, DLX MSG";

        // 通过队列属性设置消息过期时间，单位毫秒
        Map<String, Object> argss = new HashMap<>();
        argss.put("x-message-ttl",11000);//队列中的消息未被消费11秒后过期;但是这种方式似乎不是那么地灵活。所以RabbitMQ的消息也有单独的过期时间属性。

        // 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare("TEST_TTL_QUEUE", false, false, false, argss);

        // 对每条消息设置过期时间;这种更灵活
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2) // 持久化消息
                .contentEncoding("UTF-8")
                .expiration("10000") // TTL
                .build();

        // 此处两种方式设置消息过期时间的方式都使用了，将以较小的数值为准

        // 发送消息
        channel.basicPublish("", "TEST_TTL_QUEUE", properties, msg.getBytes());

        channel.close();
        conn.close();

        /*有了过期时间还不够，这个消息不能直接丢弃，不然就没办法消费了。最好是丢到一个容器里面，这样就可以实现延迟消费了。
            这里我们来了解一下死信的概念。
        */
    }
}

