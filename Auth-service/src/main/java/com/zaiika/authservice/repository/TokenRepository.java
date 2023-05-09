package com.zaiika.authservice.repository;

import com.zaiika.authservice.model.token.Token;
import com.zaiika.authservice.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query(value = """
            SELECT tokens.*
            FROM tokens
            JOIN users
                ON users.id = tokens.user_id
            WHERE user_id = :#{#userId}
            AND (tokens.expired = false or tokens.revoked = false)""", nativeQuery = true)
    List<Token> findAllValidTokenByUserId(Long userId);

    @Query(value = """
            select *
            from tokens
            where token = :#{#token}
            """, nativeQuery = true)
    Token findByToken(String token);

    @Modifying
    @Transactional
    @Query(value = """
            update tokens
            set revoked = :#{#token.revoked},
                expired = :#{#token.expired}
            where id = :#{#token.id}
            """, nativeQuery = true)
    void updateToken(Token token);
}
