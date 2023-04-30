package com.zaiika.gateway.gateway.filter;

import com.zaiika.gateway.gateway.validator.Validator;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final Validator<ServerHttpRequest> validator;
    private final RabbitTemplate template;

    @Autowired
    public AuthFilter(Validator<ServerHttpRequest> validator, RabbitTemplate template) {
        super(Config.class);
        this.validator = validator;
        this.template = template;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!validator.validate(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new IllegalArgumentException("Missing authorization header");
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                token = token.substring(7);

                var isValidToken = template.convertSendAndReceive(
                        "auth.exchange",
                        "auth.token.key",
                        new Message(token.getBytes(StandardCharsets.UTF_8)));

                if (isValidToken == null || !((boolean) isValidToken)) {
                    throw new IllegalArgumentException("Token not valid");
                }
            }

            return chain.filter(exchange);
        };
    }

    public static final class Config {
    }
}
