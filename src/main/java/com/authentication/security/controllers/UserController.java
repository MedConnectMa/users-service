package com.authentication.security.controllers;

import com.authentication.security.models.user.UserInfo;
import com.authentication.security.models.user.UserUpdateRequest;
import com.authentication.security.responseMessage.ResponseMsg;
import com.authentication.security.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;


@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getUserId(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        try {
            Optional<UserInfo> userOptional = userService.getUser(token);

            if (userOptional.isPresent()) {
                UserInfo userInfo = userOptional.get();
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.BAD_REQUEST.value(), "User Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (HttpClientErrorException.Forbidden e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.FORBIDDEN.value(), "You are not authorized to access the resource");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        try {
            userService.deleteUser(token);
            ResponseMsg okResponse = new ResponseMsg(HttpStatus.OK.value(), "User deleted successfully");
            return ResponseEntity.ok(okResponse);
        } catch (IllegalArgumentException e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.BAD_REQUEST.value(), "User Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IllegalStateException e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.FORBIDDEN.value(), token);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token,
                                        @RequestBody UserUpdateRequest userUpdateRequest)
    {
        token = token.substring(7);
        try {
            userService.updateUser(token, userUpdateRequest);
            ResponseMsg okResponse = new ResponseMsg(HttpStatus.OK.value(), "User updated Successfully" );
            return ResponseEntity.ok(okResponse);
        }catch (Exception e){
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.BAD_REQUEST.value(), "User Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (IllegalAccessError e) {
            ResponseMsg errorResponse = new ResponseMsg(HttpStatus.FORBIDDEN.value(), "You are not authorized to update this user.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }
}
