package com.project.zaiika.services.userServices;

import com.project.zaiika.models.userModels.User;
import com.project.zaiika.repositories.userRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
@Primary
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUserInfo() {
//        var authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return repository.findByLogin(((UserDetailImpl) authUser).getLogin());
        return null;
    }

    @Override
    public User getUserByLogin(String login) {
        return repository.findUserByLogin(login);
    }
}
