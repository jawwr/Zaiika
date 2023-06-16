package com.zaiika.workerservice.service.permission;

import com.zaiika.users.UserServiceGrpc;
import com.zaiika.users.UserServiceOuterClass;
import com.zaiika.workerservice.service.token.TokenService;
import com.zaiika.workerservice.service.user.UserService;
import com.zaiika.workerservice.service.worker.WorkerService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("customPermissionEvaluator")
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {
    private final TokenService tokenService;
    private final UserService userService;
    private final WorkerService workerService;
    @Value("${grpc.server.user-service.port}")
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
//        ManagedChannel channel = ManagedChannelBuilder
//                .forTarget("localhost:" + grpcServerPort)
//                .usePlaintext()
//                .build();
//
//        var stub = UserServiceGrpc.newBlockingStub(channel);
//
//        var request = UserServiceOuterClass.RoleRequest
//                .newBuilder()
//                .setToken(tokenService.getToken())
//                .setRole(permission)
//                .build();
//        var response = stub.hasRole(request);
//        channel.shutdownNow();
        var user = userService.getUser();
        return workerService.hasPermission(user.id(), permission);//response.getHasRole();
    }
}
