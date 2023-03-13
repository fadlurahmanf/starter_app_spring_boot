package com.fadlurahmanf.starter.amqp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class RabbitMQConsumerService {
    Logger logger = LoggerFactory.getLogger(RabbitMQConsumerService.class);

    @RabbitListener(queues = "${starter_app.rabbitmq.queue}")
    public void consume(String message) throws Exception {
        logger.info("Received message: " + message + " at -> " + LocalDateTime.now());
        if(Objects.equals(message, "TES ERROR")){
            throw new Exception("NAMANYA SALAH NIH");
        }
    }
}
