package com.zaiika.authservice.service.userService;

import com.google.protobuf.Empty;
import com.zaiika.authservice.model.user.User;
import com.zaiika.authservice.model.user.role.Role;
import com.zaiika.authservice.model.worker.Worker;
import com.zaiika.authservice.repository.RoleRepository;
import com.zaiika.authservice.repository.UserJpaRepository;
import com.zaiika.authservice.repository.WorkerRepository;
import com.zaiika.authservice.service.jwtService.TokenService;
import com.zaiika.users.UserServiceGrpc;
import com.zaiika.users.UserServiceOuterClass;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase implements UserService {
    private final UserJpaRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void setRoleToUser(Long userId, String roleName) {
        var role = findRoleByName(roleName);
        roleRepository.setRoleToUser(userId, role);
    }

    private Role findRoleByName(String roleName) {
        var role = roleRepository.findRoleByName(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Role does not exist");
        }

        return role;
    }

    @Override
    @Transactional
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
    public boolean hasUserRole(String token, String roleName) {
        var user = tokenService.getUserByToken(token);
        return roleRepository.hasRole(user.getId(), roleName);
    }

    @Override
    public User getUserInfo(String token) {
        token = token.substring(7);
        return tokenService.getUserByToken(token);
    }

    @Override
    public void hasRole(UserServiceOuterClass.RoleRequest request,
                        StreamObserver<UserServiceOuterClass.HasRoleResponse> responseObserver) {
        var hasRole = hasUserRole(request.getToken(), request.getRole());
        var response = UserServiceOuterClass.HasRoleResponse
                .newBuilder()
                .setHasRole(hasRole)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional
    public void setRoleForUser(UserServiceOuterClass.RoleRequest request,
                               StreamObserver<Empty> responseObserver) {
        var user = tokenService.getUserByToken(request.getToken());
        setRoleToUser(user.getId(), request.getRole());

        var response = Empty.getDefaultInstance();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserInfo(UserServiceOuterClass.TokenRequest request,
                            StreamObserver<UserServiceOuterClass.UserInfoResponse> responseObserver) {
        var user = tokenService.getUserByToken(request.getToken());

        var response = UserServiceOuterClass.UserInfoResponse
                .newBuilder()
                .setId(user.getId())
                .setLogin(user.getLogin() == null ? "" : user.getLogin())
                .setName(user.getName() == null ? "" : user.getName())
                .setSurname(user.getSurname() == null ? "" : user.getSurname())
                .setPatronymic(user.getPatronymic() == null ? "" : user.getPatronymic())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteRoleFromUser(UserServiceOuterClass.RoleRequest request,
                                   StreamObserver<Empty> responseObserver) {
        var user = tokenService.getUserByToken(request.getToken());
        deleteRoleFromUser(user.getId(), request.getRole());

        var response = Empty.getDefaultInstance();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void saveWorkerAsUser(UserServiceOuterClass.WorkerCredentialsRequest request,
                                 StreamObserver<UserServiceOuterClass.UserResponse> responseObserver) {
        validatePinCode(request.getPlaceId(), request.getPin());
        var user = User
                .builder()
                .name(request.getName())
                .surname(request.getSurname())
                .patronymic(request.getPatronymic())
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPin()))
                .build();

        var savedUser = userRepository.save(user);
        var response = UserServiceOuterClass.UserResponse
                .newBuilder()
                .setUserId(savedUser.getId())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private void validatePinCode(long placeId, String pin) {
        var workers = workerRepository.findAllByPlaceId(placeId);
        var users = workers.stream().map(Worker::getUser).toList();
        for (var user : users) {
            if (passwordEncoder.matches(pin, user.getPassword())) {
                throw new IllegalArgumentException("Pin code already exist");
            }
        }
    }
}
