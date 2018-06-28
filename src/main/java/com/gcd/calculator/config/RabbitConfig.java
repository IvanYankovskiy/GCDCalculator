package com.gcd.calculator.config;

import com.gcd.calculator.messaging.ResultSender;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@ComponentScan(basePackages = "{com.gcd.calculator.controller}")
public class RabbitConfig {    
    @Value("${messaging.rabbit.url}")
    private String hostUrl;
    
    @Value("${messaging.exchange.name}")
    private String exchangeName;
    
    @Value("${messaging.sender.key}")
    private String senderRoutingKey;
    
    @Value("${messaging.sender.queue}")
    private String senderQueue;
    
    @Value("${messaging.reciever.key}")
    private String recieverRoutingKey;
    
    @Value("${messaging.reciever.queue}")
    private String recieverQueue;
    
    @Value("${messaging.reciever.maxConcurrentConsumers}")
    private int maxConcurrentConsumers;
    
    @Value("${messaging.reciever.incoming.class}")
    private String mappableMessageClassName;
    
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
            new CachingConnectionFactory(hostUrl);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate =  new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        rabbitTemplate.setExchange(exchangeName);
        return rabbitTemplate;
    }
    
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName, true, false);
    } 
    
    @Bean
    public Queue taskQueue() {
       return new Queue(recieverQueue,true);
    }
    
    @Bean
    public Queue resultQueue() {
        return new Queue(senderQueue, true);
    }
    
    @Bean
    public ResultSender resultSender() {
        return new ResultSender(senderRoutingKey);
    }
    
    @Bean
    public Binding bindingTask(DirectExchange exchange, Queue taskQueue) {
        return BindingBuilder.bind(taskQueue)
                .to(exchange)
                .with(recieverRoutingKey);
    }
    
    @Bean
    public Binding bindingResult(DirectExchange exchange, Queue resultQueue) {
        return BindingBuilder.bind(resultQueue())
                .to(exchange)
                .with(senderRoutingKey);
    }
    
    @Bean
    public SimpleRabbitListenerContainerFactory myRabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory 
                = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setIdleEventInterval(60000L);
        factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
        factory.setMessageConverter(jackson2MessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(Boolean.FALSE);
        return factory;
    }
    
    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter() {
        Jackson2JsonMessageConverter messageConverter = new Jackson2JsonMessageConverter();
        return messageConverter;
    }
}
