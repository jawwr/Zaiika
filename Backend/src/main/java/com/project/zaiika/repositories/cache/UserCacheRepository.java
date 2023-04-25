package com.project.zaiika.repositories.cache;

import com.project.zaiika.models.user.UserCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    public static final String HASH_KEY = "users";
    private final RedisTemplate<String, Object> template;

    public void save(UserCache cache) {
        template.opsForHash().put(HASH_KEY, cache.login(), cache);
    }

    public UserCache getUserByLogin(String key) {
        return (UserCache) template.opsForHash().get(HASH_KEY, key);
    }

    public void updateUser(String key, UserCache cache) {
        template.opsForHash().delete(HASH_KEY, key);
        this.save(cache);
    }

    public void deleteUser(String key) {
        template.opsForHash().delete(key);
    }
}
