package com.zaiika.workerservice.service.permission;

import com.zaiika.workerservice.service.user.UserService;
import com.zaiika.workerservice.service.worker.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("customPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final UserService userService;
    private final WorkerService workerService;

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
        var user = userService.getUser();
        return workerService.hasPermission(user.id(), permission);
    }
}
