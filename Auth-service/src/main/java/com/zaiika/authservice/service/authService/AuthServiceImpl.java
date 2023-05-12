package com.zaiika.authservice.service.authService;

import com.zaiika.authservice.model.authCredentials.LoginCredential;
import com.zaiika.authservice.model.authCredentials.RegisterCredential;
import com.zaiika.authservice.model.token.TokenResponse;
import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.model.user.UserDetailImpl;
import com.zaiika.authservice.model.user.role.Role;
import com.zaiika.authservice.model.user.role.UserRole;
import com.zaiika.authservice.model.worker.Worker;
import com.zaiika.authservice.model.worker.WorkerCredential;
import com.zaiika.authservice.repository.RoleRepository;
import com.zaiika.authservice.repository.UserJpaRepository;
import com.zaiika.authservice.repository.WorkerRepository;
import com.zaiika.authservice.service.jwtService.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserJpaRepository userRepository;
    private final RoleRepository roleRepository;
    private final WorkerRepository workerRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    @Transactional
    public TokenResponse register(RegisterCredential credential) {
        if (userRepository.existUserByLogin(credential.login())) {
            throw new IllegalArgumentException("User already exist");
        }
        var user = convertCredentialsToUser(credential);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(findRoleByName(UserRole.USER.name())));

        var savedUser = userRepository.save(user);
        var jwt = tokenService.generateToken(user.getLogin());
        tokenService.saveUserToken(jwt, savedUser);

        return new TokenResponse(jwt);
    }

    private Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }

    private User convertCredentialsToUser(RegisterCredential credential) {
        return User.builder()
                .name(credential.name())
                .surname(credential.surname())
                .login(credential.login())
                .password(credential.password())
                .build();
    }

    @Override
    @Transactional
    public TokenResponse login(LoginCredential credential) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credential.login(),
                        credential.password()
                )
        );

        var user = userRepository.findUserByLogin(credential.login());
        var jwt = tokenService.generateToken(user.getLogin());
        tokenService.revokeAllUserTokens(user);
        tokenService.saveUserToken(jwt, user);

        return new TokenResponse(jwt);
    }

    @Override
    @Transactional
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

        var jwt = tokenService.generateToken(userDetail.getLogin());
        tokenService.revokeAllUserTokens(workerUser);
        tokenService.saveUserToken(jwt, workerUser);

        return new TokenResponse(jwt);
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenService.isTokenValid(token);
    }

    @Override
    public User getUser(String token) {
        return tokenService.getUserByToken(token);
    }
}
