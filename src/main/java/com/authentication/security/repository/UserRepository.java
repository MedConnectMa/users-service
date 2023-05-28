package com.authentication.security.repository;

import com.authentication.security.models.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    @Modifying
    @Query("UPDATE User u SET u.fullName = :fullName, " +
            "u.phone = :phone, " +
            "u.cin = :cin WHERE u.id = :userId")
    void updateUserInfo(@Param("userId") Integer userId,
                        @Param("fullName") String fullName,
                        @Param("phone") String phone,
                        @Param("cin") String cin);
}
