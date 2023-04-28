package com.project.zaiika.repositories.cache;

public interface CacheRepository<T, ID> {
    void save(T cache);

    T getById(ID id);

    void update(ID key, T cache);

    void delete(ID key);
}
