package com.zaiika.placeservice.service.users;

import com.zaiika.placeservice.repository.TokenDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter
public class TokenRepo {
    private TokenDto tokenDto;
}
