package com.gcd.calculator.messaging;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class ResultSender {
    private static Logger logger = LoggerFactory.getLogger(ResultSender.class);
    
    private RabbitTemplate rabbitTemplate;
    private DirectExchange exchange;
    private final String routingKey;
    
    public ResultSender(String routingKey) {
        this.routingKey = routingKey;
    }
    
    public void send(ObjectNode taskResult) {
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, taskResult);
        logger.debug(" [x] Send to api '" + taskResult + "'");
    }

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    @Autowired
    public void setExchange(DirectExchange exchange) {
        this.exchange = exchange;
    }
    
    
}
