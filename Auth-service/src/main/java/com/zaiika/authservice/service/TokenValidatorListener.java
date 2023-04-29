package com.zaiika.authservice.service;

import com.zaiika.authservice.config.RabbitConfig;
import com.zaiika.authservice.model.UserDetailImpl;
import com.zaiika.authservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
@RequiredArgsConstructor
public class TokenValidatorListener {
    private JwtService jwtService;
    private TokenRepository tokenRepository;
    private UserDetailServiceImpl userDetailService;

    @RabbitListener(queues = RabbitConfig.VALIDATE_TOKEN_QUEUE_NAME)
    public boolean validateToken(String token) {
        var login = jwtService.extractLogin(token);
        if (login == null) {
            return false;
        }
        var savedToken = tokenRepository.findByToken(token);
        var user = (UserDetailImpl) userDetailService.loadUserByUsername(login);
        var isTokenValid = !savedToken.isRevoked() && !savedToken.isExpired();
        return jwtService.isTokenValid(token, user) && isTokenValid;
    }
}
