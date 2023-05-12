package com.zaiika.placeservice.service.users;

import com.zaiika.placeservice.model.utils.UserDto;
import com.zaiika.users.UserServiceGrpc;
import com.zaiika.users.UserServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    private final CacheManager cacheManager;
    private static final String CACHE_NAME = "userToken";
    @Value("${grpc.server.port}")
    private int grpcServerPort;

    @Override
    public UserDto getUser() {
        var cache = getUserFromCache();
        if (cache != null) {
            return cache;
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenService.getToken());
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        var response = restTemplate.exchange(
                        "http://localhost:8765/api/users/userInfo",
                        HttpMethod.GET,
                        entity,
                        UserDto.class)
                .getBody();

        saveUserToCache(response);

        return response;
    }

    private UserDto getUserFromCache() {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(tokenService.getToken(), UserDto.class);
    }

    private void saveUserToCache(UserDto userDto) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return;
        }
        cache.put(tokenService.getToken(), userDto);
    }

    @Override
    public void setRole(String roleName) {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + grpcServerPort)
                .usePlaintext()
                .build();

        var stub = UserServiceGrpc.newBlockingStub(channel);

        var request = UserServiceOuterClass.RoleRequest
                .newBuilder()
                .setRole(roleName)
                .setToken(tokenService.getToken())
                .build();

        stub.setRoleForUser(request);
        channel.shutdownNow();
    }

    @Override
    public void deleteRole(String roleName) {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + grpcServerPort)
                .usePlaintext()
                .build();

        var stub = UserServiceGrpc.newBlockingStub(channel);

        var request = UserServiceOuterClass.RoleRequest
                .newBuilder()
                .setRole(roleName)
                .setToken(tokenService.getToken())
                .build();

        stub.deleteRoleFromUser(request);
        channel.shutdownNow();
    }
}