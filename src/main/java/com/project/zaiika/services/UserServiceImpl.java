package com.project.zaiika.services;

import com.project.zaiika.models.User;
import com.project.zaiika.models.UserDetailImpl;
import com.project.zaiika.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUserInfo() {
        var authUser = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findByLogin(((UserDetailImpl) authUser).getLogin());
    }

    @Override
    public User getUserByLogin(String login) {
        return repository.findByLogin(login);
    }
}
