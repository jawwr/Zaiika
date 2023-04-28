package com.project.zaiika.utils.cache;

import com.project.zaiika.models.user.UserCache;
import com.project.zaiika.repositories.user.cache.UserRedisCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersCache {
    private final UserRedisCacheRepository userCacheRepository;

    public void cacheUser(UserCache cache) {
        userCacheRepository.save(cache);
    }
}
