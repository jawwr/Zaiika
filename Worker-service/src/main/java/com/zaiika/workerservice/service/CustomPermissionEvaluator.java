package com.zaiika.workerservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Component("customPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object token,
                                 Object permission) {
        return hasPermission((String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return hasPermission((String) permission);
    }

    private boolean hasPermission(String permission) {
        var token = tokenService.getToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        var hasPermission = restTemplate.exchange(
                        "http://localhost:8765/api/users/hasPermission?pName=" + permission,
                        HttpMethod.GET,
                        entity,
                        Boolean.class)
                .getBody();
        if (hasPermission == null) {
            return false;
        }
        return hasPermission;
    }
}
