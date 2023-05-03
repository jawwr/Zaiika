package com.zaiika.authservice.service;

import com.zaiika.authservice.model.LoginCredential;
import com.zaiika.authservice.model.RegisterCredential;
import com.zaiika.authservice.model.TokenResponse;
import com.zaiika.authservice.model.WorkerCredential;

public interface AuthService {
    TokenResponse register(RegisterCredential credential);

    TokenResponse login(LoginCredential credential);

    TokenResponse login(long placeId, WorkerCredential credential);

    boolean isValidToken(String token);
}
