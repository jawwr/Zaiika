package com.zaiika.authservice.service.userService;

import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.model.user.role.Role;
import com.zaiika.authservice.repository.PermissionRepository;
import com.zaiika.authservice.repository.RoleRepository;
import com.zaiika.authservice.repository.UserJpaRepository;
import com.zaiika.authservice.service.jwtService.TokenService;
import com.zaiika.users.UserServiceGrpc;
import com.zaiika.users.UserServiceOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {
    private final UserJpaRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final TokenService tokenService;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void setRoleToUser(Long userId, String roleName) {
        var user = userRepository.findUserById(userId);
        var role = findRoleByName(roleName);

        user.getRoles().add(role);
        userRepository.save(user);
    }

    private Role findRoleByName(String roleName) {
        var role = roleRepository.findRoleByName(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Role does not exist");
        }

        return role;
    }

    @Override
    public void deleteRoleFromUser(long userId, String roleName) {
        var user = userRepository.findUserById(userId);
        var role = findRoleByName(roleName);

        var roles = user.getRoles();
        var userRole = roles.stream().filter(x -> x.getId() == role.getId()).findFirst().orElse(null);
        if (userRole == null) {
            throw new IllegalArgumentException("User does not have role with name " + roleName);
        }

        roles.remove(userRole);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteUserById(userId);
    }

    @Override
    public boolean hasUserPermission(String token, String permissionName) {
        token = token.substring(7);
        var user = tokenService.getUserByToken(token);
        return permissionRepository.hasPermission(user.getId(), permissionName);
    }

    @Override
    public User getUserInfo(String token) {
        token = token.substring(7);
        return tokenService.getUserByToken(token);
    }

    @Override
    public void hasPermission(UserServiceOuterClass.PermissionRequest request,
                              StreamObserver<UserServiceOuterClass.HasPermissionResponse> responseObserver) {
        var hasPermission = hasUserPermission(request.getToken(), request.getPermission());

        var response = UserServiceOuterClass.HasPermissionResponse
                .newBuilder()
                .setHasPermission(hasPermission)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserInfo(UserServiceOuterClass.UserInfoRequest request,
                            StreamObserver<UserServiceOuterClass.UserResponse> responseObserver) {
        var userInfo = getUserInfo(request.getToken());

        var response = UserServiceOuterClass.UserResponse
                .newBuilder()
                .setId(userInfo.getId())
                .setLogin(userInfo.getLogin())
                .setName(userInfo.getName())
                .setSurname(userInfo.getSurname())
                .setPatronymic(userInfo.getPatronymic())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
