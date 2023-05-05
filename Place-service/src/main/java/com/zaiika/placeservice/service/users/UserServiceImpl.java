package com.zaiika.placeservice.service.users;

import com.zaiika.placeservice.model.utils.UserDto;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final RestTemplate restTemplate;
    @Resource(name = "tokenRepo")
    private final TokenRepo tokenRepo;
    public UserDto getUser() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(tokenRepo.getTokenDto().token());
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        return restTemplate.exchange(
                        "http://localhost:8765/api/users/userInfo",
                        HttpMethod.GET,
                        entity,
                        UserDto.class)
                .getBody();
    }
}
