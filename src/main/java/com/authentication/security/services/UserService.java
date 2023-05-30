package com.authentication.security.services;

import com.authentication.security.models.user.User;
import com.authentication.security.models.user.UserUpdateRequest;
import com.authentication.security.repository.TokenRepository;
import com.authentication.security.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public Optional<Integer> getUserId(String token){
        Optional<Integer> userIdOptional = tokenRepository.findUserIdByToken(token);
        if (userIdOptional.isPresent()) {
            Integer userId = userIdOptional.get();
            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                throw new IllegalStateException("User with id " + userId + " not found");
            }
        }
        return userIdOptional;
    }

    @Transactional
    public void deleteUser(String token) {
        Optional<Integer> userIdOptional = tokenRepository.findUserIdByToken(token);
        if (userIdOptional.isPresent()) {
            Integer userId = userIdOptional.get();
            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                throw new IllegalStateException("User with id " + userId + " not found");
            }
            var validUserTokens = tokenRepository.findAllValidTokensByUser(userId);
            if(!validUserTokens.isEmpty()){
                tokenRepository.deleteAllByUserId(userId);
                userRepository.deleteById(userId);
            } else {
            throw new IllegalStateException("User has valid tokens and cannot be deleted");
            }

        } else {
            throw new IllegalArgumentException("Invalid token");
        }
    }


    @Transactional
    @Modifying
    public void updateUser(String token, UserUpdateRequest userUpdateRequest){
        Optional<Integer> userIdOptional = tokenRepository.findUserIdByToken(token);
        if (userIdOptional.isPresent()) {
            Integer userId = userIdOptional.get();
            boolean exists = userRepository.existsById(userId);
            if (!exists) {
                throw new IllegalStateException("User with id " + userId + " not found");
            }
            var validUserTokens = tokenRepository.findAllValidTokensByUser(userId);
            if(!validUserTokens.isEmpty()){
                userRepository.updateUserInfo(userId, userUpdateRequest.getFullName(),
                                            userUpdateRequest.getPhone(),
                                            userUpdateRequest.getCin());
            }
        }
    }
}
