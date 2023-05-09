package com.zaiika.authservice.service.jwtService;

import com.zaiika.authservice.model.token.Token;
import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.repository.TokenRepository;
import com.zaiika.token.TokenServiceGrpc;
import com.zaiika.token.TokenServiceOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl extends TokenServiceGrpc.TokenServiceImplBase implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final CacheManager cacheManager;
    private static final String CACHE_NAMES = "usersToken";

    @Override
    @Cacheable(cacheNames = {CACHE_NAMES}, key = "#token")
    public User getUserByToken(String token) {
        return tokenRepository.findByToken(token).getUser();
    }

    @Override
    public boolean isTokenValid(String token) {
        var cache = cacheManager.getCache(CACHE_NAMES);
        User cacheUser = null;
        if (cache != null) {
            cacheUser = cache.get(token, User.class);
        }

        var user = cacheUser == null
                ? getUserByToken(token)
                : cacheUser;
        if (user == null) {
            return false;
        }
        return jwtService.isTokenValid(token, user.getLogin());
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
        var cache = cacheManager.getCache(CACHE_NAMES);
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
        var token = new Token(jwtToken, user);
        tokenRepository.save(token);
    }

    @Override
    @Transactional
    public void isValid(TokenServiceOuterClass.TokenRequest request,
                        StreamObserver<TokenServiceOuterClass.IsTokenValidResponse> responseObserver) {
        boolean isValid;
        try {
            isValid = isTokenValid(request.getToken());
        } catch (Exception e) {
            log.error(e.getMessage());
            isValid = false;
        }
        var response = TokenServiceOuterClass.IsTokenValidResponse
                .newBuilder()
                .setIsValid(isValid)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
