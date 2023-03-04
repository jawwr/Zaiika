package com.project.zaiika.models.auth;

import com.project.zaiika.models.userModels.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterCredential {
    private String login;
    private String password;
    private String email;
    private String name;
    private String surname;

    public User convertToUser() {
        return User.builder()
                .name(this.name)
                .surname(this.surname)
                .login(this.login)
                .password(this.password)
                .build();
    }
}
