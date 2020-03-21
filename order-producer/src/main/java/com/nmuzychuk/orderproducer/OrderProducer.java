package com.nmuzychuk.orderproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class OrderProducer {

    private final JmsTemplate jmsTemplate;
    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);
    private final ObjectMapper jacksonObjectMapper;
    private final ApplicationContext applicationContext;
    private final List<String> products = Arrays.asList(
            "laptop", "phone", "printer", "tablet", "monitor", "headphones", "speakers", "keyboard", "mouse"
    );

    public OrderProducer(JmsTemplate jmsTemplate, ObjectMapper jacksonObjectMapper,
                         ApplicationContext applicationContext) {
        this.jmsTemplate = jmsTemplate;
        this.jacksonObjectMapper = jacksonObjectMapper;
        this.applicationContext = applicationContext;
    }

    @Scheduled(fixedRate = 1)
    public void createOrder() throws JsonProcessingException {
        Collections.shuffle(products);
        Order order = new Order(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE),
                products.subList(ThreadLocalRandom.current().nextInt(products.size() / 2),
                        ThreadLocalRandom.current().nextInt(products.size() / 2) + products.size() / 2),
                ThreadLocalRandom.current().nextInt(2000));
        logger.debug("Sending order: " + order);
        jmsTemplate.convertAndSend("Orders", jacksonObjectMapper.writeValueAsString(order));
    }

    @Scheduled(fixedRate = 5000, initialDelay = 1000)
    public void exit() {
        SpringApplication.exit(applicationContext, () -> 0);
    }
}
