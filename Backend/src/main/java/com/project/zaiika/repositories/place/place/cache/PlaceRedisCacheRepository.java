package com.project.zaiika.repositories.place.place.cache;

import com.project.zaiika.models.placeModels.PlaceCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlaceRedisCacheRepository implements PlaceCacheRepository {
    protected static final String HASH_KEY = "places";
    protected final HashOperations<String, Long, Object> ops;

    @Override
    public void save(PlaceCache cache) {
        ops.put(HASH_KEY, cache.id(), cache);
    }

    @Override
    public PlaceCache getById(Long id) {
        return (PlaceCache) ops.get(HASH_KEY, id);
    }

    @Override
    public void update(Long key, PlaceCache cache) {
        ops.put(HASH_KEY, key, cache);
    }

    @Override
    public void delete(Long key) {
        ops.delete(HASH_KEY, key);
    }
}
