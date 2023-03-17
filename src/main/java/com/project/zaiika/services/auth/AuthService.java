package com.project.zaiika.services.auth;

import com.project.zaiika.models.auth.LoginCredential;
import com.project.zaiika.models.auth.RegisterCredential;
import com.project.zaiika.models.auth.WorkerCredential;
import com.project.zaiika.models.token.Token;
import com.project.zaiika.models.token.TokenResponse;
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

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WorkerRepository workerRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public TokenResponse register(RegisterCredential credential) {
        if (userRepository.existsUserByLogin(credential.getLogin())) {
            throw new IllegalArgumentException("User already exist");
        }
        var user = credential.convertToUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var role = roleRepository.findRoleByName(UserRole.USER.name());
        user.setRole(role);
        user.setRoleId(role.getId());
        var savedUser = userRepository.save(user);
        var jwt = jwtService.generateToken(UserDetailImpl.build(user));
        saveUserToken(jwt, savedUser);

        return new TokenResponse(jwt);
    }

    private void saveUserToken(String jwtToken, User user) {
        var token = new Token(-1L, jwtToken, false, false, user);
        tokenRepository.save(token);
    }

    public TokenResponse login(LoginCredential credential) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credential.getLogin(),
                        credential.getPassword()
                )
        );

        var user = userRepository.findUserByLogin(credential.getLogin());
        var jwt = jwtService.generateToken(UserDetailImpl.build(user));
        revokeAllUserTokens(user);
        saveUserToken(jwt, user);

        return new TokenResponse(jwt);
    }

    public TokenResponse login(long placeId, WorkerCredential credential) {
        var workers = workerRepository.findAllByPlaceId(placeId);
        var users = userRepository.findUserByIds(workers.stream().map(Worker::getUserId).collect(Collectors.toList()));
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
