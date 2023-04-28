package com.project.zaiika.repositories.user.cache;

import com.project.zaiika.models.user.UserCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRedisCacheRepository implements UserCacheRepository {
    protected static final String HASH_KEY = "users";
    private final HashOperations<String, String, Object> ops;

    @Override
    public void save(UserCache cache) {
        ops.put(HASH_KEY, cache.login(), cache);
    }

    @Override
    public UserCache getById(String key) {
        return (UserCache) ops.get(HASH_KEY, key);
    }

    @Override
    public void update(String key, UserCache cache) {
        ops.delete(HASH_KEY, key);
        this.save(cache);
    }

    @Override
    public void delete(String key) {
        ops.delete(HASH_KEY, key);
    }
}
