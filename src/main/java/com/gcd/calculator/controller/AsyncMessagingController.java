package com.gcd.calculator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gcd.calculator.messaging.ResultSender;
import com.gcd.calculator.service.GCDCalculator;
import com.gcd.calculator.taskvalidation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AsyncMessagingController {
    private static final Logger logger = LoggerFactory.getLogger(AsyncMessagingController.class);
    @Autowired
    private GCDCalculator calculator;
    
    @Autowired
    private Validator validator;
    
    @Autowired
    private ResultSender resultSender;
    
    public void send(ObjectNode taskResult) {
        resultSender.send(taskResult);
    }
    
    @RabbitListener(containerFactory = "myRabbitListenerContainerFactory", queues = "taskQueue")
    public void recieve(JsonNode task) {
        logger.debug("[x] Consumed task: '" + task + "'");
        ObjectNode validated = validate((ObjectNode)task);
        if (validated.get("status").asText().equals("ERROR")) {
            send(validated);
        } else {
            send(calculator.calculate(validated));
        }
    }
    
    public ObjectNode validate(ObjectNode task) {
        return validator.validateTask(task);
    }
    
    
}
