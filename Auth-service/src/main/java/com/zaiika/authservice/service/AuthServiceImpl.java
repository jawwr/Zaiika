package com.zaiika.authservice.service;

import com.zaiika.authservice.model.*;
import com.zaiika.authservice.repository.RoleRepository;
import com.zaiika.authservice.repository.TokenRepository;
import com.zaiika.authservice.repository.UserJpaRepository;
import com.zaiika.authservice.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserJpaRepository userRepository;
    private final RoleRepository roleRepository;
    private final WorkerRepository workerRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public TokenResponse register(RegisterCredential credential) {
        if (userRepository.existUserByLogin(credential.login())) {
            throw new IllegalArgumentException("User already exist");
        }
        var role = roleRepository.findRoleByName(UserRole.USER.name());
        var user = convertCredentialsToUser(credential, role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoles(List.of(role));
        var savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(user.getLogin());
        saveUserToken(jwt, savedUser);

        return new TokenResponse(jwt);
    }

    private User convertCredentialsToUser(RegisterCredential credential, Role role) {
        return User.builder()
                .name(credential.name())
                .surname(credential.surname())
                .login(credential.login())
                .password(credential.password())
                .roles(List.of(role))
                .build();
    }

    private void saveUserToken(String jwtToken, User user) {
        var token = new Token(jwtToken, false, false, user);
        tokenRepository.save(token);
    }

    @Override
    public TokenResponse login(LoginCredential credential) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credential.login(),
                        credential.password()
                )
        );

        var user = userRepository.findUserByLogin(credential.login());
        var jwt = jwtService.generateToken(user.getLogin());
        revokeAllUserTokens(user);
        saveUserToken(jwt, user);

        return new TokenResponse(jwt);
    }

    @Override
    public TokenResponse login(long placeId, WorkerCredential credential) {
        var workers = workerRepository.findAllByPlaceId(placeId);
        var users = workers.stream().map(Worker::getUser).toList();
        User workerUser = null;
        for (User user : users) {
            if (passwordEncoder.matches(credential.pin(), user.getPassword())) {
                workerUser = user;
            }
        }
        if (workerUser == null) {
            throw new IllegalArgumentException("Worker with this pin not exist");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        workerUser.getLogin(),
                        credential.pin()
                )
        );
        var userDetail = UserDetailImpl.of(workerUser);

        var jwt = jwtService.generateToken(userDetail.getLogin());
        revokeAllUserTokens(workerUser);
        saveUserToken(jwt, workerUser);

        return new TokenResponse(jwt);
    }

    @Override
    public boolean isValidToken(String token) {
        var savedToken = tokenRepository.findByToken(token);
        if (savedToken == null || savedToken.getUser() == null) {
            return false;
        }
        return jwtService.isTokenValid(token, savedToken.getUser().getLogin());
    }

    private void revokeAllUserTokens(User user) {
        var userTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (userTokens.isEmpty()) {
            return;
        }

        userTokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        userTokens.forEach(tokenRepository::updateToken);
    }
}
