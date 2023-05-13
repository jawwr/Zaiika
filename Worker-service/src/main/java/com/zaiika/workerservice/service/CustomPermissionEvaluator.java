package com.zaiika.workerservice.service;

import com.zaiika.users.UserServiceGrpc;
import com.zaiika.users.UserServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Component("customPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    @Value("${grpc.server.port}")
    private int grpcServerPort;

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object token,
                                 Object permission) {
        return hasPermission((String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        return hasPermission((String) permission);
    }

    private boolean hasPermission(String permission) {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + grpcServerPort)
                .usePlaintext()
                .build();

        var stub = UserServiceGrpc.newBlockingStub(channel);

        var request = UserServiceOuterClass.PermissionRequest
                .newBuilder()
                .setToken(tokenService.getToken())
                .setPermission(permission)
                .build();

        var response = stub.hasPermission(request);
        channel.shutdownNow();
        return response.getHasPermission();

//        var token = tokenService.getToken();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setBearerAuth(token);
//        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);
//
//        var hasPermission = restTemplate.exchange(
//                        "http://localhost:8765/api/users/hasPermission?pName=" + permission,
//                        HttpMethod.GET,
//                        entity,
//                        Boolean.class)
//                .getBody();
//        if (hasPermission == null) {
//            return false;
//        }
//        return true;
    }
}
