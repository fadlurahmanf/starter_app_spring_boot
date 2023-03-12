package com.fadlurahmanf.starter.amqp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumerService {
    Logger logger = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    @RabbitListener(queues = "${starter_app.rabbitmq.queue}")
    public void consume(String message){
        logger.info("Received message: " + message);
        System.out.println("Received message print: " + message);
    }
}
