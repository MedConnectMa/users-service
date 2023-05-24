package com.authentication.security.services;

import com.authentication.security.models.user.User;
import com.authentication.security.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public List<User> getUsers(){
        return userRepository.findAll();
    }

}
