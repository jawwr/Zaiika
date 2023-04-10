package com.project.zaiika.services.permission;

import lombok.NonNull;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PermissionService implements PermissionEvaluator {
    @Override
    public boolean hasPermission(@NonNull Authentication auth,
                                 @NonNull Object targetDomainObject,
                                 @NonNull Object permission) {
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

        return hasPermission(auth, targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(@NonNull Authentication auth,
                                 @NonNull Serializable targetId,
                                 @NonNull String targetType,
                                 @NonNull Object permission) {
        return hasPermission(auth, targetType.toUpperCase(), permission.toString().toUpperCase());
    }

    private boolean hasPermission(Authentication auth, String targetType, String permission) {
        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            if (grantedAuth.getAuthority().startsWith(targetType) && grantedAuth.getAuthority().contains(permission)) {
                return true;
            }
        }
        return false;
    }
}
