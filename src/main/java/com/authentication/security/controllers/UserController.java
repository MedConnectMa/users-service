package com.authentication.security.controllers;


import com.authentication.security.dto.UserDTO;
import com.authentication.security.models.user.User;
import com.authentication.security.repository.UserRepository;
import com.authentication.security.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    private UserRepository userRepository;
    @GetMapping
    public List<UserDTO> getUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        for(User user : users){
            UserDTO userDTO = new UserDTO();
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        try {
            userService.deleteUser(token);
            return ResponseEntity.ok("User deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User not found.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this user.");
        }
    }
}
