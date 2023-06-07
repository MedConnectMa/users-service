package com.authentication.security.controllers;


import com.authentication.security.dto.UserDTO;
import com.authentication.security.models.user.UserInfo;
import com.authentication.security.models.user.UserUpdateRequest;
import com.authentication.security.responseMessage.ResponseMsg;
import com.authentication.security.models.user.User;
import com.authentication.security.repository.UserRepository;
import com.authentication.security.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UsersController {
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
            userDTO.setGender(user.getGender());
            userDTO.setPhone(user.getPhone());
            userDTO.setCin(user.getCin());
            userDTO.setAddress(user.getAddress());
            userDTOs.add(userDTO);
        }
        return userDTOs;
    }

    //Get User by Id
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id){
        try {
            Optional<UserInfo> userOptional = userService.getUserById(id);

            if (userOptional.isPresent()) {
                UserInfo userInfo = userOptional.get();
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.NOT_FOUND.value(), "User Not Found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (HttpClientErrorException.Forbidden e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.FORBIDDEN.value(), "You are not authorized to access the resource");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }



}
