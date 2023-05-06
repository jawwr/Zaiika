package com.zaiika.workerservice.service;

import com.zaiika.workerservice.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final TokenService tokenService;
    private final RestTemplate restTemplate;

    @Override
    public UserDto getUser() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenService.getToken());
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                        "http://localhost:8765/api/users/userInfo",
                        HttpMethod.GET,
                        entity,
                        UserDto.class)
                .getBody();
    }
}
