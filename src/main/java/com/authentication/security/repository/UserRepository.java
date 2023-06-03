package com.authentication.security.repository;

import com.authentication.security.models.user.User;
import com.authentication.security.models.user.UserInfo;
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
            "u.cin = :cin," +
            "u.address = :address " +
            "WHERE u.id = :userId")
    void updateUserInfo(@Param("userId") Integer userId,
                        @Param("fullName") String fullName,
                        @Param("phone") String phone,
                        @Param("cin") String cin,
                        @Param("address") String address);


    @Query("SELECT new com.authentication.security.models.user.UserInfo(u.id, u.fullName, u.email, u.gender, u.phone, u.cin, u.address) " +
            "FROM User u WHERE u.id = :id")
    Optional<UserInfo> findUserDetailsById(int id);


}
