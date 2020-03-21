package com.nmuzychuk.orderconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderConsumer {

    private final Logger logger = LoggerFactory.getLogger(OrderConsumer.class);
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper jacksonObjectMapper;

    public OrderConsumer(JmsTemplate jmsTemplate, ObjectMapper jacksonObjectMapper) {
        this.jmsTemplate = jmsTemplate;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @JmsListener(destination = "Orders")
    public void consumeOrder(String orderString) throws JsonProcessingException {
        Order order = jacksonObjectMapper.readValue(orderString, Order.class);
        logger.debug("Consumed order " + order);
        jmsTemplate.convertAndSend(order.getId());
    }
}
