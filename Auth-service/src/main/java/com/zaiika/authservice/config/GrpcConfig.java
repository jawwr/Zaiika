package com.zaiika.authservice.config;

import com.zaiika.token.TokenServiceGrpc;
import com.zaiika.users.UserServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    @Bean
    public Server grpcServer(
            TokenServiceGrpc.TokenServiceImplBase tokenService,
            UserServiceGrpc.UserServiceImplBase userService
    ) {
        return ServerBuilder
                .forPort(8001)
                .addService(tokenService)
                .addService(userService)
                .build();
    }
}
