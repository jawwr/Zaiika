package com.project.zaiika.services.auth;

import com.project.zaiika.models.auth.LoginCredential;
import com.project.zaiika.models.auth.RegisterCredential;
import com.project.zaiika.models.auth.WorkerCredential;
import com.project.zaiika.models.token.Token;
import com.project.zaiika.models.token.TokenResponse;
import com.project.zaiika.models.userModels.Role;
import com.project.zaiika.models.userModels.User;
import com.project.zaiika.models.userModels.UserDetailImpl;
import com.project.zaiika.models.userModels.UserRole;
import com.project.zaiika.models.worker.Worker;
import com.project.zaiika.repositories.token.TokenRepository;
import com.project.zaiika.repositories.userRepositories.RoleRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import com.project.zaiika.repositories.worker.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WorkerRepository workerRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public TokenResponse register(RegisterCredential credential) {
        if (userRepository.existsUserByLogin(credential.login())) {
            throw new IllegalArgumentException("User already exist");
        }
        var role = roleRepository.findRoleByName(UserRole.USER.name());
        var user = convertCredentialsToUser(credential, role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRole(role);
        var savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(UserDetailImpl.build(user));
        saveUserToken(jwt, savedUser);

        return new TokenResponse(jwt);
    }

    private User convertCredentialsToUser(RegisterCredential credential, Role role) {
        return User.builder()
                .name(credential.name())
                .surname(credential.surname())
                .login(credential.login())
                .password(credential.password())
                .role(role)
                .build();
    }

    private void saveUserToken(String jwtToken, User user) {
        var token = new Token(-1L, jwtToken, false, false, user);
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
        var jwt = jwtService.generateToken(UserDetailImpl.build(user));
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
        var userDetail = UserDetailImpl.build(workerUser);

        var jwt = jwtService.generateToken(userDetail);
        revokeAllUserTokens(workerUser);
        saveUserToken(jwt, workerUser);

        return new TokenResponse(jwt);
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

        tokenRepository.saveAll(userTokens);
    }
}