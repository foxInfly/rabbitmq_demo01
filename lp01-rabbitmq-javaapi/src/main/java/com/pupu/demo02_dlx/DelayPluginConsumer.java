package com.pupu.demo02_dlx;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *   使用延时插件实现的消息投递-消费者
 *   必须要在服务端安装rabbitmq-delayed-message-exchange插件，安装步骤见README.MD
 *   先启动消费者
 *
 * @author lp
 * @since 2021/2/1 23:05
 */
public class DelayPluginConsumer {

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://guest:guest@114.55.95.30:5673");
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        //1. 声明x-delayed-message类型的exchange
        Map<String, Object> argss = new HashMap<>();
        argss.put("x-delayed-type", "direct");
        channel.exchangeDeclare("DELAY_EXCHANGE", "x-delayed-message", false, false, argss);

        //2. 声明队列
        channel.queueDeclare("DELAY_QUEUE", false, false, false, null);

        //3. 绑定交换机与队列
        channel.queueBind("DELAY_QUEUE", "DELAY_EXCHANGE", "DELAY_KEY");

        //4. 创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body){
                String msg = new String(body, StandardCharsets.UTF_8);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                System.out.println("收到消息：[" + msg + "]\n接收时间：" + sf.format(new Date())+"\n");
            }
        };

        // 开始获取消息
        // String queue, boolean autoAck, Consumer callback
        channel.basicConsume("DELAY_QUEUE", true, consumer);
    }
}