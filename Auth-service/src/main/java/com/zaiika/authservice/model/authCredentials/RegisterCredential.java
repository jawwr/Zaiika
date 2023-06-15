package com.zaiika.authservice.model.authCredentials;

public record RegisterCredential(String login,
                                 String password,
                                 String name,
                                 String surname) {
}
