package com.zaiika.placeservice.service.users;

import com.zaiika.placeservice.model.utils.UserDto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${grpc.server.port}")
    private int port;

    public UserDto getUser() {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + port)
                .usePlaintext()
                .build();

        //TODO доделать grpc


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenService.getToken());
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                        "http://localhost:8765/api/users/userInfo",
                        HttpMethod.GET,
                        entity,
                        UserDto.class)
                .getBody();
    }
}
