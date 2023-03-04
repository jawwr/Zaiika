package com.project.zaiika.services.auth;

import com.project.zaiika.models.auth.LoginCredential;
import com.project.zaiika.models.auth.RegisterCredential;
import com.project.zaiika.models.userModels.UserDetailImpl;
import com.project.zaiika.models.userModels.UserRole;
import com.project.zaiika.repositories.userRepositories.RoleRepository;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterCredential credential) {
        var user = credential.convertToUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var role = roleRepository.findRoleByName(UserRole.USER);
        user.setRole(role);
        user.setRoleId(role.getId());
        userRepository.save(user);

        return jwtService.generateToken(UserDetailImpl.build(user));
    }

    public String login(LoginCredential credential) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credential.getLogin(),
                        credential.getPassword()
                )
        );

        var user = userRepository.findUserByLogin(credential.getLogin());
        return jwtService.generateToken(UserDetailImpl.build(user));
    }
}
