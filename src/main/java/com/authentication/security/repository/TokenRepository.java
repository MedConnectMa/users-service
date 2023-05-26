package com.authentication.security.repository;

import com.authentication.security.models.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
    select t from Token t inner join User u on t.user.id = u.id
    where u.id = :userId and (t.expired = false or t.revoked = false)
""")
    List<Token> findAllValidTokensByUser(Integer userId);

    Optional<Token> findByToken(String token);
    @Query("""
    SELECT u.id FROM Token t JOIN t.user u WHERE t.token = :token
""")
    Optional<Integer> findUserIdByToken(String token);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);

}
