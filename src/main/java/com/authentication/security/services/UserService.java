package com.authentication.security.services;

import com.authentication.security.models.user.User;
import com.authentication.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(String token){
        int currentUserId = jwtService.getCurrentUserId(token);
        boolean exist = userRepository.existsById(currentUserId);
        if(!exist){
            throw new IllegalStateException(
                    "User with id " + currentUserId + "not found"
            );
        }
        userRepository.deleteById(currentUserId);

    }

}
