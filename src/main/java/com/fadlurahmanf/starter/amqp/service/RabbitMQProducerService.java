package com.fadlurahmanf.starter.amqp.service;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Envelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducerService {
    Logger logger = LoggerFactory.getLogger(RabbitMQProducerService.class);

    @Value("${starter_app.rabbitmq.queue}")
    private String queue;

    @Value("${starter_app.rabbitmq.exchange}")
    private String exchange;

    @Value("${starter_app.rabbitmq.routingkey}")
    private String routingKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducerService(RabbitTemplate template){
        rabbitTemplate = template;
    }

    public void sendMessage(String message){
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setDelay(10000);
                return message;
            }
        });
    }
}
