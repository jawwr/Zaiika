package com.example.userservice.repository;

import com.example.userservice.model.User;
import com.example.userservice.model.UserCache;
import com.example.userservice.model.permission.Permission;
import com.example.userservice.repository.permissions.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    //    private final UserCacheRepository userCacheRepository;
    private final UserJpaRepository userJpaRepository;
    //    private final WorkerRepository workerRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Override
    public User getUserByLogin(String login) {
//        var userCache = userCacheRepository.getById(login);
//        if (userCache != null) {
//            return convertUserCacheToUser(userCache);
//        }
        var savedUser = userJpaRepository.findUserByLogin(login);
        savedUser.setPermissions(getUserPermissions(savedUser.getId()));

//        var cache = convertUserToUserCache(savedUser);

//        userCacheRepository.save(cache);
        return savedUser;
    }

    @Override
    public User getUserById(long id) {
        var user = userJpaRepository.findUserById(id);
        user.setRoles(roleRepository.findUserRole(user.getId()));
        return user;
    }

    @Override
    public boolean existUserByLogin(String login) {
//        var user = userCacheRepository.getById(login);
//        if (user != null) {
//            return true;
//        }
        return userJpaRepository.existUserByLogin(login);
    }

    @Override
    public User save(User user) {

        return userJpaRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userJpaRepository.findAll();
    }

    @Override
    public void deleteUserById(long id) {
        var deleteUser = userJpaRepository.deleteUserById(id);
//        userCacheRepository.delete(deleteUser.getLogin());
    }

    private UserCache convertUserToUserCache(User user) {
        return UserCache.builder()
                .id(user.getId())
                .login(user.getLogin())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .patronymic(user.getPatronymic())
                .permissions(user.getPermissions())
                .build();
    }

    private User convertUserCacheToUser(UserCache cache) {
        return User.builder()
                .id(cache.id())
                .name(cache.name())
                .surname(cache.surname())
                .patronymic(cache.patronymic())
                .login(cache.login())
                .password(cache.password())
                .permissions(cache.permissions())
                .roles(new ArrayList<>())
                .build();
    }

    private List<Permission> getUserPermissions(long userId) {
        List<Permission> permissions = getPermissions(userId);
        permissions.addAll(getWorkerPermission(userId));
        permissions.addAll(getRolesAsPermissions(userId));

        return permissions;
    }

    private List<Permission> getPermissions(long userId) {
        return permissionRepository.getUserPermission(userId);
    }

    private List<Permission> getWorkerPermission(long userId) {
        List<Permission> permissions = new ArrayList<>();
//        if (!workerRepository.isWorkerExist(userId)) {
//            return permissions;
//        }
//        var worker = workerRepository.findWorkerByUserId(userId);
//        var placeRole = worker.getPlaceRole();
//
//        permissions.addAll(placeRole.getPermissions());
        return permissions;
    }

    private List<Permission> getRolesAsPermissions(long userId) {
        List<Permission> permissions = new ArrayList<>();
        var roles = roleRepository.findUserRole(userId);

        var rolesAsPermission = roles.stream().map(role -> new Permission(role.getName())).toList();

        permissions.addAll(rolesAsPermission);

        return permissions;
    }
}
