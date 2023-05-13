package com.zaiika.workerservice.service.user;

import com.zaiika.users.UserServiceGrpc;
import com.zaiika.users.UserServiceOuterClass;
import com.zaiika.workerservice.model.UserDto;
import com.zaiika.workerservice.model.WorkerCredentials;
import com.zaiika.workerservice.repository.WorkerRepository;
import com.zaiika.workerservice.service.token.TokenService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final TokenService tokenService;
    private final CacheManager cacheManager;
    private final WorkerRepository workerRepository;
    @Value("${grpc.server.user-service.port}")
    private int grpcUserServicePort;
    private static final String CACHE_NAME = "userToken";

    @Override
    public UserDto getUser() {
        var cacheUser = getUserDtoFromCache(tokenService.getToken());
        if (cacheUser != null) {
            return cacheUser;
        }
        var userId = getUserIdFromAuthService();
        var placeId = workerRepository.getWorkerByUserId(userId).getPlaceId();

        var dto = new UserDto(userId, placeId);
        saveUserDtoToCache(dto, tokenService.getToken());
        return dto;
    }

    @Override
    public long saveWorker(WorkerCredentials worker) {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + grpcUserServicePort)
                .usePlaintext()
                .build();

        var stub = UserServiceGrpc.newBlockingStub(channel);
        var request = UserServiceOuterClass.WorkerCredentialsRequest
                .newBuilder()
                .setLogin(worker.login())
                .setName(worker.name())
                .setSurname(worker.surname())
                .setPatronymic(worker.patronymic())
                .setPin(worker.pin())
                .setPlaceId(worker.placeId())
                .build();

        var response = stub.saveWorkerAsUser(request);
        channel.shutdownNow();
        return response.getUserId();
    }

    private long getUserIdFromAuthService() {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + grpcUserServicePort)
                .usePlaintext()
                .build();

        var stub = UserServiceGrpc.newBlockingStub(channel);

        var request = UserServiceOuterClass.TokenRequest
                .newBuilder()
                .setToken(tokenService.getToken())
                .build();

        var response = stub.getUserInfo(request);

        return response.getId();
    }

    private UserDto getUserDtoFromCache(String token) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return null;
        }
        return cache.get(token, UserDto.class);
    }

    private void saveUserDtoToCache(UserDto userDto, String token) {
        var cache = cacheManager.getCache(CACHE_NAME);
        if (cache == null) {
            return;
        }
        cache.put(token, userDto);
    }
}
