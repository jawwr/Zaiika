package com.zaiika.gateway.gateway.config;

import com.zaiika.gateway.gateway.validator.RouteValidator;
import com.zaiika.gateway.gateway.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;

@Configuration
public class AppConfig {
    @Bean
    public Validator<ServerHttpRequest> httpRequestValidator() {
        return RouteValidator.builder()
                .addRoutes("/api/order")
                .build();
    }
}
