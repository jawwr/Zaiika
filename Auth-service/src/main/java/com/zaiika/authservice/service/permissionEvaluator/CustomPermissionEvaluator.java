package com.zaiika.authservice.service.permissionEvaluator;

import com.zaiika.authservice.repository.RoleRepository;
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

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object userId,
                                 Object permission) {
        return roleRepository.hasRole((long) userId, (String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return true;
    }
}
