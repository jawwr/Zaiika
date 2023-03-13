package com.project.zaiika.repositories.token;

import com.project.zaiika.models.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    Token findByToken(String token);
}