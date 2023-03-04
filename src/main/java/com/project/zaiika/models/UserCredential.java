package com.project.zaiika.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCredential {
    private String login;
    private String password;

    public static UserCredential convertUserToUserCredential(User user) {
        return new UserCredential(user.getLogin(), user.getPassword());
    }
}
