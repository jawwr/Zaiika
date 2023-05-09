package com.zaiika.authservice.utils;

import io.grpc.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GrpcUtils {
    private final Server server;

    @EventListener(ApplicationStartedEvent.class)
    public void startGrpcServer() {
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Grpc server starts on " + server.getPort() + " port");
    }
}
