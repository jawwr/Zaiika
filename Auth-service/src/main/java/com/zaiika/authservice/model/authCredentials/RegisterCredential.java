package com.zaiika.authservice.model.authCredentials;

public record RegisterCredential(String login,
                                 String password,
                                 String email,
                                 String name,
                                 String surname) {
}
