package com.authentication.security.controllers;


import com.authentication.security.models.user.User;
import com.authentication.security.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }



}
