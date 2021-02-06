package com.pupu.demo06_confirm;

import com.pupu.util.ResourceUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者，测试batch Confirm模式
 *
 * @author lp
 * @since 2021/2/1 17:45
 **/
public class Demo2_BatchConfirmProducer {
    private final static String QUEUE_NAME = "ORIGIN_QUEUE";

    public static void main(String[] args) throws Exception {

        //1. 根据uri创建连接工厂,连接，获取通道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(ResourceUtil.getKey("rabbitmq.uri"));
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        //2. 声明消息内容
        String msg = "Hello world, Rabbit MQ ,Batch Confirm";

        //3. 声明队列（默认交换机AMQP default，Direct）
        // String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        try {
            //4. 开启发送方确认模式
            channel.confirmSelect();
            for (int i = 0; i < 2; i++) {
                //5. 发送消息
                // String exchange, String routingKey, BasicProperties props, byte[] body
                channel.basicPublish("", QUEUE_NAME, null, (msg + "-" + i).getBytes());
            }
            // 批量确认结果，ACK如果是Multiple=True，代表ACK里面的Delivery-Tag之前的消息都被确认了
            // 比如5条消息可能只收到1个ACK，也可能收到2个（抓包才看得到）
            // 直到所有信息都发布，只要有一个未被Broker确认就会IOException
            channel.waitForConfirmsOrDie();
            System.out.println("消息发送完毕，批量确认成功");
        } catch (Exception e) {
            // 发生异常，可能需要对所有消息进行重发
            System.out.println("消息发送失败");
            e.printStackTrace();
        }

        channel.close();
        conn.close();
    }
}

