package com.zaiika.gateway.gateway.config;

import com.zaiika.gateway.gateway.validator.RouteValidator;
import com.zaiika.gateway.gateway.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public Validator<ServerHttpRequest> httpRequestValidator() {
        return RouteValidator.builder()
                .addRoutes(
                        "/api/",
                        "/api/order/",
                        "/api/users/"
                )
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
