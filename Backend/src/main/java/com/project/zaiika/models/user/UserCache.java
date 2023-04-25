package com.project.zaiika.models.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

@RedisHash("CacheUser")
public record UserCache(
        @Id
        long id,
        String name,
        String surname,
        String patronymic,
        String login,
        List<GrantedAuthority> authorities
) implements Serializable {
}
