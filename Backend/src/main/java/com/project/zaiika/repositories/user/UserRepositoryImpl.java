package com.project.zaiika.repositories.user;

import com.project.zaiika.models.user.User;
import com.project.zaiika.models.user.UserCache;
import com.project.zaiika.repositories.cache.UserCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserCacheRepository userCacheRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public User getUserByLogin(String login) {
        var userCache = userCacheRepository.getUserByLogin(login);
        if (userCache != null) {
            return convertUserCacheToUser(userCache);
        }
        return userJpaRepository.findUserByLogin(login);
    }

    @Override
    public User getUserById(long id) {
        return userJpaRepository.findUserById(id);
    }

    @Override
    public boolean existUserByLogin(String login) {
        var user = userCacheRepository.getUserByLogin(login);
        if (user != null) {
            return true;
        }
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
        userCacheRepository.deleteUser(deleteUser.getLogin());
    }

    private User convertUserCacheToUser(UserCache cache) {
        return User.builder()
                .id(cache.id())
                .name(cache.name())
                .surname(cache.surname())
                .patronymic(cache.patronymic())
                .login(cache.login())
                .build();
    }
}
