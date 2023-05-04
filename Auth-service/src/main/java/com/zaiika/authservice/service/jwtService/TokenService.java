package com.zaiika.authservice.service.jwtService;

import com.zaiika.authservice.model.user.User;

public interface TokenService {
    User getUserByToken(String token);

    boolean isTokenValid(String token);

    String generateToken(String userLogin);

    void revokeAllUserTokens(User user);

    void saveUserToken(String jwtToken, User user);
}
