package com.zaiika.gateway.gateway.service;

import com.zaiika.token.TokenServiceGrpc;
import com.zaiika.token.TokenServiceOuterClass;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenServiceImpl implements TokenService {
    @Value("${grpc.server.port}")
    private int port;

    @Override
    public boolean isValid(String token) {
        ManagedChannel channel = ManagedChannelBuilder
                .forTarget("localhost:" + port)
                .usePlaintext()
                .build();

        var stub = TokenServiceGrpc.newBlockingStub(channel);

        var request = TokenServiceOuterClass.TokenRequest
                .newBuilder()
                .setToken(token)
                .build();

        var response = stub.isValid(request);
        channel.shutdownNow();

        return response.getIsValid();
    }
}
