package com.zaiika.placeservice.service.permission;

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

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object token,
                                 Object permission) {
        return hasPermission((String) token, (String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return hasPermission((String) targetId, (String) permission);
    }

    private boolean hasPermission(String token, String permission) {
        HttpHeaders httpHeaders = new HttpHeaders();
        var authToken = token.substring(7);
        httpHeaders.setBearerAuth(authToken);
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        var hasPermission = restTemplate.exchange(
                        "http://localhost:8765/api/manage-users/hasPermission?pName=" + permission,
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
