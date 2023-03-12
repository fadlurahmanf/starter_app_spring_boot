package com.fadlurahmanf.starter.amqp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQService {
    Logger logger = LoggerFactory.getLogger(RabbitMQService.class);

    @Value("${starter_app.rabbitmq.queue}")
    private String queue;

    @Value("${starter_app.rabbitmq.exchange}")
    private String exchange;

    @Value("${starter_app.rabbitmq.routingkey}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQService(RabbitTemplate template){
        rabbitTemplate = template;
    }

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
