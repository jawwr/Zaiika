package com.zaiika.placeservice.service.permission;

import com.zaiika.placeservice.service.users.UserService;
import com.zaiika.worker.WorkerServiceGrpc;
import com.zaiika.worker.WorkerServiceOuterClass;
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
    private final UserService userService;
    @Value("${grpc.server.worker-service.port}")
    private int grpcWorkerServerPort;

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
        var user = userService.getUser();
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + grpcWorkerServerPort)
                .usePlaintext()
                .build();

        var stub = WorkerServiceGrpc.newBlockingStub(channel);

        var request = WorkerServiceOuterClass.HasPermissionRequest
                .newBuilder()
                .setUserId(user.id())
                .setPermission(permission)
                .build();

        var response = stub.hasPermission(request);
        channel.shutdownNow();
        return response.getHasPermission();
    }
}
