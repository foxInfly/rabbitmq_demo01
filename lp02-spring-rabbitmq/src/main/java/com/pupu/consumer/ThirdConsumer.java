package com.pupu.consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author lp
 * @since  2021/2/1 23:24
 */
public class ThirdConsumer implements MessageListener {
    private Logger logger = LoggerFactory.getLogger(ThirdConsumer.class);

    public void onMessage(Message message) {
        logger.info("The third cosumer received message : " + message);
    }
}

