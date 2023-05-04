package com.zaiika.authservice.service.jwtService;

import com.zaiika.authservice.model.token.Token;
import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = {"usersToken"}, key = "#token")
    public User getUserByToken(String token) {
        log.info("get user by token");
        return tokenRepository.findByToken(token).getUser();
    }

    @Override
    public boolean isTokenValid(String token) {
        var savedToken = tokenRepository.findByToken(token);
        if (savedToken == null || savedToken.getUser() == null) {
            return false;
        }
        return jwtService.isTokenValid(token, savedToken.getUser().getLogin());
    }

    @Override
    public String generateToken(String userLogin) {
        return jwtService.generateToken(userLogin);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        var userTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (userTokens.isEmpty()) {
            return;
        }
        var cache = cacheManager.getCache("usersToken");
        userTokens.forEach(token -> {

            if (cache != null) {
                cache.evict(token.getToken());
            }

            token.setRevoked(true);
            token.setExpired(true);
        });

        userTokens.forEach(tokenRepository::updateToken);
    }

    @Override
    public void saveUserToken(String jwtToken, User user) {
        var token = new Token(jwtToken, false, false, user);
        tokenRepository.save(token);
    }
}
