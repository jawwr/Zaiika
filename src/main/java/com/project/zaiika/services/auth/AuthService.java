package com.project.zaiika.services.auth;

import com.project.zaiika.models.auth.LoginCredential;
import com.project.zaiika.models.auth.RegisterCredential;
import com.project.zaiika.models.auth.WorkerCredential;
import com.project.zaiika.models.token.TokenResponse;

public interface AuthService {
    TokenResponse register(RegisterCredential credential);

    TokenResponse login(LoginCredential credential);
    TokenResponse login(long placeId, WorkerCredential credential);
}
