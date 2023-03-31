package com.project.zaiika.utils;

import com.project.zaiika.models.roles.Role;
import com.project.zaiika.models.roles.UserRole;
import com.project.zaiika.repositories.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbUtils {
    private final RoleRepository repository;

    @EventListener(ApplicationReadyEvent.class)
    public void createRoles() {
        List<Role> roles = new ArrayList<>();
        var availableRoles = UserRole.values();
        for (long i = 1; i <= availableRoles.length; i++) {
            var name = availableRoles[(int) (i - 1)].name();
            if (!repository.existsByIdAndName(i, name)) {
                var role = new Role(i, name);
                roles.add(role);
            }
        }

        if (roles.size() > 0) {
            repository.saveAll(roles);
        }
    }
}
