package com.project.zaiika.repositories.userRepositories;

import com.project.zaiika.models.userModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByLogin(String login);

    boolean existsUserByLogin(String login);
}
