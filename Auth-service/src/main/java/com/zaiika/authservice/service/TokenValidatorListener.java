package com.zaiika.authservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaiika.authservice.config.RabbitConfig;
import com.zaiika.authservice.model.UserDetailImpl;
import com.zaiika.authservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.glassfish.jaxb.core.v2.model.core.TypeRef;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class TokenValidatorListener {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailServiceImpl userDetailService;

    @RabbitListener(queues = RabbitConfig.VALIDATE_TOKEN_QUEUE_NAME)
    public Message validateToken(Message message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Map<String, String>> mapType = new TypeReference<>() {
        };
        Map<String, String> token = mapper.readValue(message.getBody(), mapType);
        var receiveToken = token.get("token");
        try {
            var login = jwtService.extractLogin(receiveToken);

            if (login == null) {
                return new Message("false".getBytes(StandardCharsets.UTF_8));
            }

            var savedToken = tokenRepository.findByToken(receiveToken);
            var user = (UserDetailImpl) userDetailService.loadUserByUsername(login);

            var isTokenValid = !savedToken.isRevoked() && !savedToken.isExpired();
            var result = jwtService.isTokenValid(receiveToken, user) && isTokenValid;
            return result
                    ? new Message("true".getBytes(StandardCharsets.UTF_8))
                    : new Message("false".getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new Message("false".getBytes(StandardCharsets.UTF_8));
        }
    }

    //    @RabbitListener(queues = RabbitConfig.VALIDATE_TOKEN_QUEUE_NAME)
    public void a(Message mess) {
        System.out.println(new String(mess.getBody()));
    }
}
