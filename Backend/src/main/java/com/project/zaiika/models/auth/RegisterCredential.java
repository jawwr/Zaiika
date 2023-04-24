package com.project.zaiika.models.auth;

public record RegisterCredential(String login,
                                 String password,
                                 String email,
                                 String name,
                                 String surname) {
}
