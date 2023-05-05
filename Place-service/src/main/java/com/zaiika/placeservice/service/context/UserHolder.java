package com.zaiika.placeservice.service.context;

import com.zaiika.placeservice.model.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Getter
@Setter
public class UserHolder {
    private UserDto dto;
}
