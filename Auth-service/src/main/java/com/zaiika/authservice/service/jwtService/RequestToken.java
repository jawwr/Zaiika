package com.zaiika.authservice.service.jwtService;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestToken {
    private String token;

    public String getToken() {
        return token;
    }

    public void save(String token) {
        this.token = token;
    }
}
