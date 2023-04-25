package com.project.zaiika.utils.cache;

import com.project.zaiika.repositories.cache.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsersCache {
    private final UserCacheRepository userCacheRepository;

    public void cacheUser(UsersCache cache) {
        System.out.println(cache);
    }
}
