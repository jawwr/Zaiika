package com.zaiika.workerservice.config;

import com.zaiika.worker.WorkerServiceGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {
    @Value("${grpc.server.worker-service.port}")
    private int serverPort;

    @Bean
    public Server grpcServer(WorkerServiceGrpc.WorkerServiceImplBase workerService) {
        return ServerBuilder
                .forPort(serverPort)
                .addService(workerService)
                .build();
    }
}
