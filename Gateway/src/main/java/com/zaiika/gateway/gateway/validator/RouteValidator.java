package com.zaiika.gateway.gateway.validator;

import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteValidator implements Validator<ServerHttpRequest> {
    private final List<String> routes;
    private final List<String> exclude;

    private RouteValidator(List<String> routes, List<String> exclude) {
        this.routes = routes;
        this.exclude = exclude;
    }

    @Override
    public boolean validate(ServerHttpRequest value) {
        boolean isExcluded = this.exclude.stream().anyMatch(x -> value.getURI().getPath().contains(x));
        if (isExcluded){
            return false;
        }
        return routes.stream().anyMatch(x -> value.getURI().getPath().contains(x));
    }

    public static RouteValidatorBuilder builder() {
        return new RouteValidatorBuilder();
    }

    public static final class RouteValidatorBuilder {
        private final List<String> routes;
        private final List<String> exclude;

        private RouteValidatorBuilder() {
            this.routes = new ArrayList<>();
            this.exclude = new ArrayList<>();
        }

        public RouteValidatorBuilder addRoutes(String... routes) {
            this.routes.addAll(List.of(routes));
            return this;
        }

        public RouteValidatorBuilder addExclude(String... routes) {
            boolean isNotContains = Arrays.stream(routes).noneMatch(this.routes::contains);
            if (!isNotContains) {
                throw new IllegalArgumentException("Rotes can't be includes and exclude at the same time");
            }
            this.exclude.addAll(List.of(routes));
            return this;
        }

        public RouteValidator build() {
            return new RouteValidator(routes, exclude);
        }
    }
}
