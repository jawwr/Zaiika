package com.zaiika.gateway.gateway.config;

import com.zaiika.gateway.gateway.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Configuration
public class AuthConfig {
    @Value("${server.port}")
    private int port;

    @Value("${server.hostname}")
    private String host;

    @Bean
    public GatewayFilter gatewayFilter(AuthFilter authFilter) {
        return authFilter.apply(new AuthFilter.Config());
    }

    @Bean
    public RouteLocator locator(RouteLocatorBuilder builder, GatewayFilter filter) {
        return builder.routes()
                .route(rout -> rout.path("/api/order/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://order-service")
                )
                .route(rout -> rout.path("/api/delivery/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://delivery-service")
                )
                .route(rout -> rout.path("/api/manage-place/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://place-service")
                )
                .route(rout -> rout.path("/api/site/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://place-service")
                )
                .route(rout -> rout.path("/api/menu/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://place-service")
                )
                .route(rout -> rout.path("/api/sites/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://place-service")
                )
                .route(rout -> rout.path("/api/workers/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://worker-service")
                )
                .route(rout -> rout.path("/api/user/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://user-service")
                )
                .route(rout -> rout.path("/api/auth/**")
                        .filters(filterSpec -> filterSpec.filter(filter))
                        .uri("lb://auth-service")
                )
                .route(route -> route.path(
                                "/order-service/**",
                                "/delivery-service/**",
                                "/auth-service/**",
                                "/place-service/**",
                                "/worker-service/**",
                                "/user-service/**")
                        .filters(filterSpec -> filterSpec.changeRequestUri(exchange -> {
                            var serverRequest = exchange.getRequest();
                            var path = serverRequest.getPath();
                            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUri(serverRequest.getURI())
                                    .host(host)
                                    .port(port)
                                    .replacePath(path.subPath(2).toString());

                            return Optional.of(uriBuilder.build().toUri());
                        }))
                        .uri("lb://ignored-uri")
                )
                .build();
    }
}
