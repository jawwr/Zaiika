package com.zaiika.authservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String AUTH_QUEUE_NAME = "auth";
    public static final String VALIDATE_TOKEN_QUEUE_NAME = "validateToken";
    public static final String QUEUE_EXCHANGE = "auth.exchange";
    public static final String AUTH_QUEUE_KEY = "auth.key";
    public static final String TOKEN_QUEUE_KEY = "auth.token.key";

    @Value("${rabbitmq.host}")
    private String host;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(host);
    }

    @Bean
    public AmqpAdmin admin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        var template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public Queue authQueue() {
        return new Queue(AUTH_QUEUE_NAME);
    }

    @Bean
    public Queue validateTokenQueue() {
        return new Queue(VALIDATE_TOKEN_QUEUE_NAME);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(QUEUE_EXCHANGE);
    }

    @Bean
    public Binding bindingAuth(TopicExchange exchange) {
        return BindingBuilder.bind(authQueue()).to(exchange).with(AUTH_QUEUE_KEY);
    }

    @Bean
    public Binding binding(TopicExchange exchange) {
        return BindingBuilder.bind(validateTokenQueue()).to(exchange).with(TOKEN_QUEUE_KEY);
    }
}
