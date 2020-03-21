package com.nmuzychuk.orderconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);

    @JmsListener(destination = "Consumer.Email.VirtualTopic.Orders")
    public void emailConsumer(String notification) {
        logger.debug("Email notification received for: " + notification);
    }

    @JmsListener(destination = "Consumer.SMS.VirtualTopic.Orders")
    public void smsConsumer(String notification) {
        logger.debug("SMS notification received for: " + notification);
    }
}
