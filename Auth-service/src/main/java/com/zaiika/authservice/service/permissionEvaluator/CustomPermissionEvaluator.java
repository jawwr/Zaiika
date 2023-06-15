package com.zaiika.authservice.service.permissionEvaluator;

import com.zaiika.authservice.repository.RoleRepository;
import com.zaiika.authservice.service.jwtService.RequestToken;
import com.zaiika.authservice.service.jwtService.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

//TODO
@Component("customPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final RequestToken requestToken;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object userId,
                                 Object permission) {
        var user = tokenService.getUserByToken(requestToken.getToken());
        return roleRepository.hasRole(user.getId(), (String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return hasPermission(authentication, targetId, permission);
    }
}
