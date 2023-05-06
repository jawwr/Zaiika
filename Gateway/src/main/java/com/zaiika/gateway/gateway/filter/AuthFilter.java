package com.zaiika.gateway.gateway.filter;

import com.zaiika.gateway.gateway.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private final Validator<ServerHttpRequest> validator;
    private final RestTemplate restTemplate;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!validator.validate(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new IllegalArgumentException("Missing authorization header");
                }

                String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                token = token.substring(7);

                Boolean isValidToken = restTemplate.getForObject(
                        "http://localhost:8765/api/auth/isValid?token=" + token,
                        Boolean.class
                );

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