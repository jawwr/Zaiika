package com.project.zaiika.services;

import com.project.zaiika.auth.JWTResponse;
import com.project.zaiika.auth.JWTUtils;
import com.project.zaiika.models.UserCredential;
import com.project.zaiika.repositories.RoleRepository;
import com.project.zaiika.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//@Service
public class AuthService {
    private AuthenticationManager manager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    JWTUtils jwtUtils;

    @Autowired
    public AuthService(AuthenticationManager manager, UserRepository userRepository, RoleRepository roleRepository, JWTUtils jwtUtils) {
        this.manager = manager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
    }

    public JWTResponse signIn(UserCredential credential) {
        Authentication authentication = manager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        credential.getLogin(),
                        credential.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new JWTResponse(jwt);
    }
}
