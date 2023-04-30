package com.zaiika.authservice.service;

import com.zaiika.authservice.config.RabbitConfig;
import com.zaiika.authservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class TokenValidatorListener {
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @RabbitListener(queues = RabbitConfig.VALIDATE_TOKEN_QUEUE_NAME)
    public Message validateToken(Message message) {
        var receiveToken = new String(message.getBody());
        try {
            var login = jwtService.extractLogin(receiveToken);

            if (login == null) {
                return new Message("false".getBytes(StandardCharsets.UTF_8));
            }

            var savedToken = tokenRepository.findByToken(receiveToken);
            var user = savedToken.getUser();

            var isTokenValid = !savedToken.isRevoked() && !savedToken.isExpired();
            var result = jwtService.isTokenValid(receiveToken, user.getLogin()) && isTokenValid;

            return new Message(Boolean.valueOf(result).toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new Message("false".getBytes(StandardCharsets.UTF_8));
        }
    }

    //    @RabbitListener(queues = RabbitConfig.VALIDATE_TOKEN_QUEUE_NAME)
    public void a(Message mess) {
        System.out.println(new String(mess.getBody()));
    }
}
