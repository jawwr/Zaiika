package com.zaiika.authservice.service.authService;

import com.zaiika.authservice.model.authCredentials.LoginCredential;
import com.zaiika.authservice.model.authCredentials.RegisterCredential;
import com.zaiika.authservice.model.token.TokenResponse;
import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.model.worker.WorkerCredential;

import javax.net.ssl.SSLSession;

public interface AuthService {
    TokenResponse register(RegisterCredential credential);

    TokenResponse login(LoginCredential credential);

    TokenResponse login(long placeId, WorkerCredential credential);

    boolean isValidToken(String token);

    User getUser(String token);
}
