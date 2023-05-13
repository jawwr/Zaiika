package com.zaiika.workerservice.service.token;

import com.zaiika.workerservice.model.TokenDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class TokenService {
    private TokenDto tokenDto;

    public void save(String token) {
        this.tokenDto = new TokenDto(token);
    }

    public String getToken() {
        return this.tokenDto.token();
    }
}
