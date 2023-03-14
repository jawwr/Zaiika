package com.project.zaiika.models.userModels;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private String login;
    private String role;
}
