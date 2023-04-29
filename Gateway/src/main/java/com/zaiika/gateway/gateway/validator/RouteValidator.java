package com.zaiika.gateway.gateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.ArrayList;
import java.util.List;

public class RouteValidator implements Validator<ServerHttpRequest> {
    private final List<String> routes;

    private RouteValidator(List<String> routes) {
        this.routes = routes;
    }

    @Override
    public boolean validate(ServerHttpRequest value) {
        return routes.stream().noneMatch(x -> value.getURI().getPath().contains(x));
    }

    public static RouteValidatorBuilder builder() {
        return new RouteValidatorBuilder();
    }

    public static final class RouteValidatorBuilder {
        private final List<String> routes;

        public RouteValidatorBuilder() {
            this.routes = new ArrayList<>();
        }

        public RouteValidatorBuilder addRoutes(String... routes) {
            this.routes.addAll(List.of(routes));
            return this;
        }

        public RouteValidator build() {
            return new RouteValidator(routes);
        }
    }
}
