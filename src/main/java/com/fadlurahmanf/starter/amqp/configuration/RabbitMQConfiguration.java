package com.fadlurahmanf.starter.amqp.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {

    @Value("${starter_app.rabbitmq.queue}")
    private String queue;

    @Value("${starter_app.rabbitmq.exchange}")
    private String exchange;

    @Value("${starter_app.rabbitmq.dl.queue}")
    private String dlQueue;

    @Value("${starter_app.rabbitmq.dl.exchange}")
    private String dlExchange;

    @Value("${starter_app.rabbitmq.routingkey}")
    private String routingKey;

    @Value("${starter_app.rabbitmq.dl.routingkey}")
    private String dlRoutingKey;

    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    @Bean
    public Queue DLQueue(){
        return new Queue(dlQueue);
    }

    @Bean
    public DirectExchange DLExchange(){
        return new DirectExchange(dlExchange);
    }

    @Bean
    public CustomExchange exchange(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(exchange, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey)
                .noargs();
    }

    @Bean
    public Binding dlBinding(){
        return BindingBuilder.bind(DLQueue())
                .to(DLExchange())
                .with(dlRoutingKey);
    }
}
