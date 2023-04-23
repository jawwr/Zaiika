package com.project.zaiika.repositories.user;

import com.project.zaiika.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    User findUserByLogin(String login);

    @Transactional
    @Query(value = """
            select id, login, name, patronymic, surname, password
            from users
            where login = :#{#userLogin}
            """, nativeQuery = true)
    User findOnlyUserByLogin(String userLogin);

    @Query(value = """
            select count(*) <> 0
            from users
            where login = :#{#login}
            """, nativeQuery = true)
    boolean existUserByLogin(String login);

    User findUserById(long id);
}
