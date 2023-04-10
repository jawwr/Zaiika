package com.project.zaiika.repositories.user;

import com.project.zaiika.models.userModels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    User findUserByLogin(String login);

    boolean existsUserByLogin(String login);

    User findUserById(long id);

    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM users WHERE id IN :#{#ids}", nativeQuery = true)
    List<User> findUserByIds(List<Long> ids);
}
